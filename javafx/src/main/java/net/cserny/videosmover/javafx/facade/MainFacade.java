package net.cserny.videosmover.javafx.facade;

import net.cserny.videosmover.core.helper.StaticPathsProvider;
import net.cserny.videosmover.core.model.Video;
import net.cserny.videosmover.core.model.VideoType;
import net.cserny.videosmover.core.service.*;
import net.cserny.videosmover.core.service.observer.VideoExistenceObserver;
import net.cserny.videosmover.javafx.model.VideoRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MainFacade {

    private final ScanService scanService;
    private final OutputResolver outputResolver;
    private final CachedMetadataService cachedTmdbService;
    private final VideoMover videoMover;
    private final VideoCleaner videoCleaner;
    private final SimpleMessageRegistry messageRegistry;

    @Autowired
    public MainFacade(ScanService scanService, OutputResolver outputResolver, CachedMetadataService cachedTmdbService,
                      VideoMover videoMover, VideoCleaner videoCleaner, SimpleMessageRegistry messageRegistry) {
        this.scanService = scanService;
        this.outputResolver = outputResolver;
        this.cachedTmdbService = cachedTmdbService;
        this.videoMover = videoMover;
        this.videoCleaner = videoCleaner;
        this.messageRegistry = messageRegistry;
    }

    public List<VideoRow> scanVideos() {
        return scanService.scan(StaticPathsProvider.getDownloadsPath()).stream()
                .map(this::buildVideoRow)
                .collect(Collectors.toList());
    }

    private VideoRow buildVideoRow(Video video) {
        VideoRow videoRow = new VideoRow(video);
        videoRow.videoTypeProperty().addListener((observable, oldValue, newValue) -> {
            handleToggleVideoType(newValue, videoRow);
        });
        return videoRow;
    }

    private void handleToggleVideoType(VideoType videoType, VideoRow videoRow) {
        videoRow.setVideoType(videoType);
        if (videoType == VideoType.NONE) {
            videoRow.setOutput(null);
            return;
        }

        VideoExistenceObserver observer = new VideoExistenceObserver();
        outputResolver.resolve(videoRow.getVideo(), Collections.singletonList(observer));
        if (observer.shouldAdjustPath()) {
            cachedTmdbService.adjustOutputAndDate(videoRow.getVideo());
        }
        videoRow.setOutput(videoRow.getVideo());
    }

    public void moveVideos(List<Video> videos) {
        boolean result = videoMover.move(videos);
        if (result) {
            videoCleaner.clean(videos);
        }
        messageRegistry.displayMessage(result ? MessageProvider.moveSuccessful() : MessageProvider.problemOccurred());
    }
}
