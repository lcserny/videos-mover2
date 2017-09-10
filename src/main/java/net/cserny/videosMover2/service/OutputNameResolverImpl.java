package net.cserny.videosMover2.service;

import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.service.parser.VideoNameParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by leonardo on 02.09.2017.
 */
@Service
public class OutputNameResolverImpl implements OutputNameResolver
{
    private List<VideoNameParser> nameParserList;

    @Autowired
    public OutputNameResolverImpl(List<VideoNameParser> nameParserList) {
        this.nameParserList = nameParserList;
    }

    @Override
    public String resolveTvShow(Video video) {
        String resolvedName = video.getInput().getFileName().toString();
        for (VideoNameParser videoNameParser : nameParserList) {
            resolvedName = videoNameParser.parseTvShow(resolvedName);
        }
        return resolvedName;
    }

    @Override
    public String resolveMovie(Video video) {
        String resolvedName = video.getInput().getFileName().toString();
        for (VideoNameParser videoNameParser : nameParserList) {
            resolvedName = videoNameParser.parseMovie(resolvedName);
        }
        return resolvedName;
    }
}
