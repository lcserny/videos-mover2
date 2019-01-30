package net.cserny.videosmover.core.service.parser;

import net.cserny.videosmover.core.model.Video;
import net.cserny.videosmover.core.model.VideoType;
import net.cserny.videosmover.core.service.MessageProvider;
import net.cserny.videosmover.core.service.SimpleMessageRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TvShowOutputVideoNameChecker implements OutputVideoNameChecker {

    private final SimpleMessageRegistry messageRegistry;
    private final Pattern pattern = Pattern.compile(
            "(.*)s(?<season>\\d{1,4})e(?<episodes>\\d{1,3}([eE-]{1,2}\\d{1,3})?)(.*)",
            Pattern.CASE_INSENSITIVE);

    @Autowired
    public TvShowOutputVideoNameChecker(SimpleMessageRegistry messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public boolean canHandle(VideoType type) {
        return type == VideoType.TVSHOW;
    }

    @Override
    public void check(Video video) {
        Matcher matcher = pattern.matcher(video.getFileName());
        if (!matcher.find()) {
            messageRegistry.displayMessage(MessageProvider.incorrectTvShowFileName(video.getFileName()));
        }
    }
}
