package net.cserny.videosmover.service;

import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoPath;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.service.observer.VideoAdjustmentObserver;
import net.cserny.videosmover.service.parser.VideoNameParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;

@Singleton
public class OutputResolver {

    private Set<VideoNameParser> nameParserList;

    @Inject
    public OutputResolver(Set<VideoNameParser> nameParserList) {
        this.nameParserList = nameParserList;
    }

    public VideoPath resolve(Video video, List<VideoAdjustmentObserver> observers) {
        String resolvedName = video.getInputPath().getFileName().toString();
        if (video.getInputPath().toString().equals(StaticPathsProvider.getDownloadsPath())) {
            resolvedName = video.getInputFilename();
        }

        VideoPath videoPath = new VideoPath();
        videoPath.setOutputFolder(resolvedName);
        videoPath.setOutputPath(video.getVideoType() == VideoType.MOVIE
                    ? StaticPathsProvider.getMoviesPath()
                    : StaticPathsProvider.getTvShowsPath());

        for (VideoNameParser videoNameParser : nameParserList) {
            switch (video.getVideoType()) {
                case MOVIE:
                    videoNameParser.parseMovie(videoPath, observers);
                    break;
                case TVSHOW:
                    videoNameParser.parseTvShow(videoPath, observers);
                    break;
            }
        }

        return videoPath;
    }
}
