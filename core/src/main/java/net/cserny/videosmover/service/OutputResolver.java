package net.cserny.videosmover.service;

import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoPath;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.service.parser.VideoNameParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Set;

@Singleton
public class OutputResolver {

    private Set<VideoNameParser> nameParserList;

    @Inject
    public OutputResolver(Set<VideoNameParser> nameParserList) {
        this.nameParserList = nameParserList;
    }

    public VideoPath resolve(Video video) {
        VideoPath videoPath = resolveSimple(video);

        for (VideoNameParser videoNameParser : nameParserList) {
            switch (video.getVideoType()) {
                case MOVIE:
                    videoNameParser.parseMovie(videoPath);
                    break;
                case TVSHOW:
                    videoNameParser.parseTvShow(videoPath);
                    break;
            }
        }

        return videoPath;
    }

    public VideoPath resolveSimple(Video video) {
        String resolvedName = video.getInputPath().getFileName().toString();
        if (video.getInputPath().toString().equals(StaticPathsProvider.getDownloadsPath())) {
            resolvedName = video.getInputFilename();
        }

        VideoPath videoPath = new VideoPath();
        if (video.getOutputPath() != null) {
            videoPath.setOutputPath(video.getOutputPath().toString());
        } else {
            videoPath.setOutputPath(video.getVideoType() == VideoType.MOVIE
                    ? StaticPathsProvider.getMoviesPath()
                    : StaticPathsProvider.getTvShowsPath());
        }

        if (video.getOutputFolderName() != null) {
            videoPath.setOutputFolder(video.getOutputFolderName());
        } else {
            videoPath.setOutputFolder(resolvedName);
        }

        return videoPath;
    }
}
