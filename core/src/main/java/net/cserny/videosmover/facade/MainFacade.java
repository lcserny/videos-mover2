package net.cserny.videosmover.facade;

import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoPath;
import net.cserny.videosmover.model.VideoRow;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.service.CachedTmdbService;
import net.cserny.videosmover.service.OutputResolver;
import net.cserny.videosmover.service.ScanService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class MainFacade {

    @Inject
    ScanService scanService;

    @Inject
    OutputResolver outputResolver;

    @Inject
    CachedTmdbService cachedTmdbService;

    @Inject
    public MainFacade(ScanService scanService, OutputResolver outputResolver, CachedTmdbService cachedTmdbService) {
        this.scanService = scanService;
        this.outputResolver = outputResolver;
        this.cachedTmdbService = cachedTmdbService;
    }

    public List<VideoRow> scanVideos() {
        return scanService.scan(StaticPathsProvider.getDownloadsPath()).stream()
                .map(this::buildVideoRow)
                .collect(Collectors.toList());
    }

    private VideoRow buildVideoRow(Video video) {
        VideoRow videoRow = new VideoRow(video);
        videoRow.setName(video.getInputFilename());
        videoRow.videoTypeProperty().addListener((observable, oldValue, newValue) -> {
            handleToggleVideoType(video, newValue, videoRow);
        });
        return videoRow;
    }

    private void handleToggleVideoType(Video video, VideoType videoType, VideoRow videoRow) {
        video.setVideoType(videoType);
        videoRow.setVideoType(videoType);

        VideoPath videoPath = VideoPath.emptyVideoPath;
        if (videoType != VideoType.NONE) {
            videoPath = outputResolver.resolve(videoRow.getVideo());
            try {
                videoPath = cachedTmdbService.searchTMDBInfo(videoPath, videoType);
            } catch (Exception ignored) { }
        }

        videoRow.setOutput(StaticPathsProvider.getPath(videoPath).toString());
        video.setOutputPath(StaticPathsProvider.getPath(videoPath.getOutputPath()));
        video.setOutputFolderName(combineOutputFolderAndYear(videoPath));
    }

    public static String combineOutputFolderAndYear(VideoPath videoPath) {
        String year = videoPath.getYear() != null && !videoPath.getYear().isEmpty() ? " (" + videoPath.getYear() + ")" : "";
        return videoPath.getOutputFolder() + year;
    }
}
