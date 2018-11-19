package net.cserny.videosmover.service.parser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.service.MessageProvider;
import net.cserny.videosmover.service.SimpleMessageRegistry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class TvShowOutputVideoNameChecker implements OutputVideoNameChecker {

    private final SimpleMessageRegistry messageRegistry;
    private final Pattern pattern = Pattern.compile(
            "(.*)s(?<season>\\d{1,4})e(?<episodes>\\d{1,3}([eE-]{1,2}\\d{1,3})?)(.*)",
            Pattern.CASE_INSENSITIVE);

    @Inject
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
