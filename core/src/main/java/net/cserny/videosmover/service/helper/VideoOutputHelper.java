package net.cserny.videosmover.service.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoOutputHelper {

    public static final Pattern RELEASE_DATE = Pattern.compile("\\({0,1}(?<year>\\d{4})(-(?<month>\\d{2})-(?<day>\\d{2})\\){0,1})?");
    public static final Pattern NAME_WITH_RELEASE_DATE = Pattern.compile("(?<name>.*)\\s" + RELEASE_DATE);

    public static String trimReleaseDate(String filename) {
        Matcher matcher = NAME_WITH_RELEASE_DATE.matcher(filename);
        if (matcher.find()) {
            filename = matcher.group("name").trim();
        }
        return filename;
    }
}
