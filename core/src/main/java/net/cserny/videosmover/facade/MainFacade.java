package net.cserny.videosmover.facade;

import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoRow;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.service.*;
import net.cserny.videosmover.service.observer.VideoExistenceObserver;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class MainFacade {

    private final ScanService scanService;
    private final OutputResolver outputResolver;
    private final CachedMetadataService cachedTmdbService;
    private final VideoMover videoMover;
    private final VideoCleaner videoCleaner;
    private final SimpleMessageRegistry messageRegistry;

    @Inject
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
        if (videoType != VideoType.NONE) {
            VideoExistenceObserver observer = new VideoExistenceObserver();
            outputResolver.resolve(videoRow.getVideo(), Collections.singletonList(observer));
            if (observer.shouldAdjustPath()) {
                cachedTmdbService.adjustOutputAndDate(videoRow.getVideo());
            }
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
