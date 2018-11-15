package net.cserny.videosmover.service.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoOutputHelper {

    public static final Pattern RELEASE_DATE = Pattern.compile("\\((?<year>\\d{4})(-(?<month>\\d{2})-(?<day>\\d{2}))?\\)$");
    public static final Pattern NAME_WITH_RELEASE_DATE = Pattern.compile("(?<name>.*) " + RELEASE_DATE);

    public static String trimReleaseDate(String filename) {
        Matcher matcher = VideoOutputHelper.NAME_WITH_RELEASE_DATE.matcher(filename);
        if (matcher.find()) {
            filename = matcher.group("name");
        }
        return filename;
    }
}
