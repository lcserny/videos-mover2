package net.cserny.videosmover.service;

import net.cserny.videosmover.model.SimpleVideoOutput;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.parser.VideoNameParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public SimpleVideoOutput resolve(Video video) {
        String resolvedName = video.getInput().getFileName().toString();
        for (VideoNameParser videoNameParser : nameParserList) {
            if (video.isMovie()) {
                resolvedName = videoNameParser.parseMovie(resolvedName);
            } else if (video.isTvShow()) {
                resolvedName = videoNameParser.parseTvShow(resolvedName);
            }
        }
        return buildVideoOutput(resolvedName);
    }

    private SimpleVideoOutput buildVideoOutput(String output) {
        String name = output.substring(output.lastIndexOf('/') + 1);
        Matcher matcher = Pattern.compile("(\\d{4})").matcher(name);
        Integer year = null;
        if (matcher.find()) {
            name = name.substring(0, matcher.start() - 2);
            year = Integer.valueOf(matcher.group());
        }
        return new SimpleVideoOutput(name, year, output);
    }
}
