package net.cserny.videosmover.service.helper;

import java.util.regex.Pattern;

public class SimpleVideoOutputHelper {

    public static final Pattern NAME_WITH_RELEASE_DATE = Pattern.compile("(?<name>.*) \\((?<year>\\d{4})(-\\d{2}-\\d{2})?\\)$");

    public static final Pattern RELEASE_DATE = Pattern.compile("\\((?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})\\)$");

    public static final Pattern YEAR_ONLY = Pattern.compile("\\((?<year>\\d{4})\\)$");

//    public static SimpleVideoOutput buildVideoOutput(String output) {
//        Path outputPath = StaticPathsProvider.getPath(output);
//        String path = outputPath.getParent().toString();
//        String name = outputPath.getFileName().toString().trim();
//
//        Matcher matcher = RELEASE_DATE_PATTERN.matcher(name);
//        Integer year = null;
//        if (matcher.find()) {
//            name = matcher.group("name");
//            year = Integer.valueOf(matcher.group("year"));
//        }
//
//        VideoType videoType = null;
//        if (path.equals(StaticPathsProvider.getMoviesPath())) {
//            videoType = VideoType.MOVIE;
//        } else if (path.equals(StaticPathsProvider.getTvShowsPath())) {
//            videoType = VideoType.TVSHOW;
//        }
//
//        return new SimpleVideoOutput(name, year, path, videoType);
//    }
//
//    public static String formatOutput(SimpleVideoOutput videoOutput, VideoMetadata videoMetadata) {
//        return StaticPathsProvider.getPath(videoOutput.getPath(), formatOutputWithoutPath(videoMetadata)).toString();
//    }
//
}
