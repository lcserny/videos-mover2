package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.service.MessageProvider;
import net.cserny.videosmover.service.SimpleMessageRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TvShowOutputVideoNameChecker implements OutputVideoNameChecker {

    private final Pattern pattern = Pattern.compile(
            "(.*)s(?<season>\\d{1,4})e(?<episodes>\\d{1,3}([eE-]{1,2}\\d{1,3})?)(.*)",
            Pattern.CASE_INSENSITIVE);

    @Autowired
    SimpleMessageRegistry messageRegistry;

    @Override
    public boolean canHandle(VideoType type) {
        return type == VideoType.TVSHOW;
    }

    @Override
    public void check(Video video) {
        Matcher matcher = pattern.matcher(video.getOutputFilename());
        if (!matcher.find()) {
            messageRegistry.add(MessageProvider.incorrectTvShowFileName(video.getInputFilename()));
        }
    }
}
