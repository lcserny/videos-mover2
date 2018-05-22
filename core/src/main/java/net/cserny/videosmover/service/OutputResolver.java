package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.parser.VideoNameParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Singleton
public class OutputResolver {

    private final Set<VideoNameParser> nameParserList;

    @Inject
    public OutputResolver(Set<VideoNameParser> nameParserList) {
        this.nameParserList = nameParserList;
    }

    public String resolve(Video video) {
        String resolvedName = video.getInput().getFileName().toString();
        for (VideoNameParser videoNameParser : nameParserList) {
            resolvedName = video.isMovie()
                    ? videoNameParser.parseMovie(resolvedName)
                    : video.isTvShow()
                        ? videoNameParser.parseTvShow(resolvedName)
                        : resolvedName;
        }
        return resolvedName;
    }
}
