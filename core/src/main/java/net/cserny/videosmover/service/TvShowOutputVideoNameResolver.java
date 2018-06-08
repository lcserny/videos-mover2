package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TvShowOutputVideoNameResolver extends AbstractOutputVideoNameResolver {

    private final Pattern pattern = Pattern.compile(
            "[.]s(?<season>\\d{1,4})e(?<episodes>\\d{1,3}([e-]\\d{1,3})+)[.]",
            Pattern.CASE_INSENSITIVE);

    @Override
    public boolean canHandle(VideoType type) {
        return type == VideoType.TVSHOW;
    }

    @Override
    public String resolve(Video video) {
        if (true) {
            throw new RuntimeException("test this");
        }

        StringBuilder resolvedName = new StringBuilder(video.getOutputFilename());
        String extension = getExtension(video);
        Matcher matcher = pattern.matcher(video.getOutputFilename());

        while (matcher.find()) {
            String season = matcher.group("season");
            String episodes = matcher.group("episodes");
            String[] singleEpisodes = episodes.split("[e-]");
            resolvedName.append(String.format("s%se%s", season, Arrays.toString(singleEpisodes)));
        }

        return resolvedName.toString() + "." + extension;
    }
}
