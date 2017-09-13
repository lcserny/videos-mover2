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
    public String resolve(Video video) {
        String resolvedName = video.getInput().getFileName().toString();
        for (VideoNameParser videoNameParser : nameParserList) {
            if (video.isMovie()) {
                resolvedName = videoNameParser.parseMovie(resolvedName);
            } else if (video.isTvShow()) {
                resolvedName = videoNameParser.parseTvShow(resolvedName);
            }
        }
        return resolvedName;
    }
}
