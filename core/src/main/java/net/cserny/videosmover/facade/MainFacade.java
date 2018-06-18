package net.cserny.videosmover.facade;

import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoPath;
import net.cserny.videosmover.model.VideoRow;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.service.*;

import javax.inject.Inject;
import javax.inject.Singleton;
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
        VideoPath videoPath = VideoPath.emptyVideoPath;
        if (videoType != VideoType.NONE) {
            videoPath = outputResolver.resolve(videoRow.getVideo());
            videoPath = cachedTmdbService.adjustVideoPath(videoPath, videoType);
        }
        videoRow.setOutput(videoPath);
    }

    public static String combineOutputFolderAndYear(VideoPath videoPath) {
        String year = videoPath.getYear() != null && !videoPath.getYear().isEmpty() ? " (" + videoPath.getYear() + ")" : "";
        return videoPath.getOutputFolder() + year;
    }

    public void moveVideos(List<Video> videos) {
        boolean result = videoMover.move(videos);
        if (result) {
            videoCleaner.clean(videos);
        }
        messageRegistry.add(result ? MessageProvider.moveSuccessful() : MessageProvider.problemOccurred());
    }
}
