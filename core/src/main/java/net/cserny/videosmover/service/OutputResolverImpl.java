package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.parser.VideoNameParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by leonardo on 02.09.2017.
 */
@Service
public class OutputResolverImpl implements OutputResolver {
    private final List<VideoNameParser> nameParserList;

    @Autowired
    public OutputResolverImpl(List<VideoNameParser> nameParserList) {
        this.nameParserList = nameParserList;
    }

    @Override
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
