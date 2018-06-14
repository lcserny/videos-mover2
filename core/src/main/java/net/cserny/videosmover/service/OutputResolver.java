package net.cserny.videosmover.service;

import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
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

    public String resolve(Video video) {
        String resolvedName = video.getInputFilename();
        for (VideoNameParser videoNameParser : nameParserList) {
            switch (video.getVideoType()) {
                case MOVIE:
                    resolvedName = videoNameParser.parseMovie(resolvedName);
                    break;
                case TVSHOW:
                    resolvedName = videoNameParser.parseTvShow(resolvedName);
                    break;
            }
        }
        return resolvedName;
    }
}
