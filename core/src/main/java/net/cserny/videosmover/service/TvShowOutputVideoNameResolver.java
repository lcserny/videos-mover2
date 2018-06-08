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
            "(?<start>.*)s(?<season>\\d{1,4})e(?<episodes>\\d{1,3}([eE-]{1,2}\\d{1,3})?)(?<end>.*)",
            Pattern.CASE_INSENSITIVE);

    @Override
    public boolean canHandle(VideoType type) {
        return type == VideoType.TVSHOW;
    }

    @Override
    public String resolve(Video video) {
        if (true) {
            throw new RuntimeException("test this!");
        }

        Matcher matcher = pattern.matcher(video.getOutputFilename());
        if (matcher.find()) {
            String first = matcher.group("first");
            String season = matcher.group("season");
            String episodes = matcher.group("episodes");
            String[] singleEpisodes = episodes.split("[e-]");
            String end = matcher.group("end");
            return String.format("%ss%se%s%s", first, season, Arrays.toString(singleEpisodes), end);
        }

        return video.getOutputFilename();
    }
}
