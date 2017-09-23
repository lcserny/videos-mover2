package net.cserny.videosmover.service.helper;

import net.cserny.videosmover.model.SimpleVideoOutput;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.service.PathsProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleVideoOutputHelper {
    public static SimpleVideoOutput buildVideoOutput(String output) {
        String path = output.substring(0, output.lastIndexOf('/'));
        String name = output.substring(output.lastIndexOf('/') + 1);
        Matcher matcher = Pattern.compile("(\\d{4})").matcher(name);
        Integer year = null;
        if (matcher.find()) {
            name = name.substring(0, matcher.start() - 2);
            year = Integer.valueOf(matcher.group());
        }

        VideoType videoType = null;
        if (path.equals(PathsProvider.getMoviesPath())) {
            videoType = VideoType.MOVIE;
        } else if (path.equals(PathsProvider.getTvShowsPath())) {
            videoType = VideoType.TVSHOW;
        }

        return new SimpleVideoOutput(name, year, path, videoType);
    }

    public static String formatOutput(SimpleVideoOutput videoOutput, VideoMetadata videoMetadata) {
        return String.format("%s/%s (%s)", videoOutput.getPath(), videoMetadata.getName(), videoMetadata.getReleaseDate());
    }

    public static String formatOutputWithoutPath(VideoMetadata videoMetadata) {
        return String.format("%s (%s)", videoMetadata.getName(), videoMetadata.getReleaseDate());
    }
}