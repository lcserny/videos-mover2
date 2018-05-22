package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.parser.VideoNameParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

public class OutputResolver {

    private final Set<VideoNameParser> nameParserList;

    @Autowired
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
