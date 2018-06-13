package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.service.parser.VideoNameParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class OutputResolver {

    private Set<VideoNameParser> nameParserList;

    @Inject
    public OutputResolver(Set<VideoNameParser> nameParserList) {
        this.nameParserList = nameParserList;
    }

    public String resolve(Video video) {
        String resolvedName = video.getInputPath().toString();
        for (VideoNameParser videoNameParser : nameParserList) {
            resolvedName = video.getVideoType() == VideoType.MOVIE
                    ? videoNameParser.parseMovie(resolvedName)
                    : video.getVideoType() == VideoType.TVSHOW
                        ? videoNameParser.parseTvShow(resolvedName)
                        : resolvedName;
        }
        return resolvedName;
    }
}
