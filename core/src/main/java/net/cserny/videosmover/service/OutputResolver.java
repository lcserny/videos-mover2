package net.cserny.videosmover.service;

import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
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

    public void resolve(Video video, List<VideoAdjustmentObserver> observers) {
        String resolvedName = video.getInputFolderName();
        if (resolvedName == null) {
            resolvedName = video.getInputFolderNameFromFileName();
        }
        video.setOutputFolderName(resolvedName);
        adjustOutputFolderName(video, observers);
        adjustOutputPath(video);
    }

    private void adjustOutputFolderName(Video video, List<VideoAdjustmentObserver> observers) {
        // TODO: improve this, 3 parser for movie and 3 for Tv, more OOP
        for (VideoNameParser videoNameParser : nameParserList) {
            switch (video.getVideoType()) {
                case MOVIE:
                    videoNameParser.parseMovie(video, observers);
                    break;
                case TVSHOW:
                    videoNameParser.parseTvShow(video, observers);
                    break;
            }
        }
    }

    private void adjustOutputPath(Video video) {
        String outputPathWithoutFolder = null;
        switch (video.getVideoType()) {
            case MOVIE:
                outputPathWithoutFolder = StaticPathsProvider.getMoviesPath();
                break;
            case TVSHOW:
                outputPathWithoutFolder = StaticPathsProvider.getTvShowsPath();
                break;
        }
        video.setOutputPath(StaticPathsProvider.getJoinedPathString(outputPathWithoutFolder,
                video.getOutputFolderName(), video.getFileName()));
    }
}
