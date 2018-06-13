package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.helper.PropertiesLoader;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class VideoNameTrimmer implements VideoNameParser {

    private final Pattern videoPattern = Pattern.compile("(.*)(\\d{4})");
    private List<String> nameTrimParts;

    @Inject
    public VideoNameTrimmer() {
        nameTrimParts = PropertiesLoader.getNameTrimParts();
    }

    @Override
    public String parseTvShow(String text) {
        String trimmed = trim(text);
        String withoutExtension = removeExtension(trimmed);
        return toCamelCase(withoutExtension);
    }

    @Override
    public String parseMovie(String text) {
        String trimmed = trim(text);
        String withoutExtension = removeExtension(trimmed);
        String camelCased = toCamelCase(withoutExtension);
        return appendYear(camelCased);
    }

    private String trim(String filename) {
        for (String part : nameTrimParts) {
            Pattern compile = Pattern.compile("(?i)(" + part + ")");
            Matcher matcher = compile.matcher(filename);
            if (!part.isEmpty() && matcher.find()) {
                filename = filename.substring(0, matcher.start()) + filename.substring(matcher.end());
            }
        }
        return filename;
    }

    private String toCamelCase(String text) {
        int splitIndex = text.lastIndexOf('/') + 1;
        String path = text.substring(0, splitIndex);
        String name = text.substring(splitIndex);
        StringBuilder camelCaseString = new StringBuilder(path);

        List<String> nameParts = Arrays.asList(stripSpecialChars(name).split("\\s+"));
        for (int i = 0; i < nameParts.size(); i++) {
            if (i != 0 && i != nameParts.size()) {
                camelCaseString.append(" ");
            }
            camelCaseString.append(toProperCase(nameParts.get(i)));
        }

        return camelCaseString.toString();
    }

    private String removeExtension(String text) {
        if (text.charAt(text.length() - 4) == '.') {
            return text.substring(0, text.length() - 5);
        }
        return text;
    }

    private String stripSpecialChars(String videoName) {
        return videoName.replaceAll("([\\[._\\]])", " ").trim();
    }

    private String toProperCase(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private String appendYear(String videoName) {
        Matcher matcher = videoPattern.matcher(videoName);
        if (matcher.find()) {
            videoName = matcher.group(1).trim();
            String yearString = matcher.group(2);
            if (yearString != null) {
                videoName = String.format("%s (%s)", videoName, yearString);
            }
        }
        return videoName;
    }
}
