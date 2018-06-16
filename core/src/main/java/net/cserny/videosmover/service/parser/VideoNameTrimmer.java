package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.helper.PropertiesLoader;
import net.cserny.videosmover.model.VideoPath;

import javax.inject.Inject;
import javax.inject.Singleton;
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
    public void parseTvShow(VideoPath videoPath) {
        String trimmed = trim(videoPath.getOutputFolder());
        String withoutExtension = removeExtension(trimmed);
        String camelCase = toCamelCase(withoutExtension);
        videoPath.setOutputFolder(camelCase);
    }

    @Override
    public void parseMovie(VideoPath videoPath) {
        parseTvShow(videoPath);
        appendYear(videoPath);
    }

    private String trim(String filename) {
        for (String part : nameTrimParts) {
            Pattern compile = Pattern.compile("(?i)(-?" + part + ")");
            Matcher matcher = compile.matcher(filename);
            if (!part.isEmpty() && matcher.find()) {
                filename = filename.substring(0, matcher.start());
            }
        }
        return filename;
    }

    private String toCamelCase(String name) {
        StringBuilder camelCaseString = new StringBuilder();
        String[] nameParts = stripSpecialChars(name).split("\\s+");
        for (int i = 0; i < nameParts.length; i++) {
            if (i != 0) {
                camelCaseString.append(" ");
            }
            camelCaseString.append(toProperCase(nameParts[i]));
        }
        return camelCaseString.toString();
    }

    private String removeExtension(String text) {
        boolean extensionPeriodExists = text.charAt(text.length() - 4) == '.';
        char firstChar = text.charAt(text.length() - 3);
        boolean extensionFirstLetter = Character.isLetter(firstChar) && Character.isLowerCase(firstChar);
        char secondChar = text.charAt(text.length() - 2);
        boolean extensionSecondLetter = Character.isLetter(secondChar) && Character.isLowerCase(secondChar);
        char thirdChar = text.charAt(text.length() - 1);
        boolean extensionThirdLetter = Character.isLetter(thirdChar) && Character.isLowerCase(thirdChar);

        if (extensionPeriodExists && extensionFirstLetter && extensionSecondLetter && extensionThirdLetter) {
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

    private void appendYear(VideoPath videoPath) {
        Matcher matcher = videoPattern.matcher(videoPath.getOutputFolder());
        if (matcher.find()) {
            if (matcher.start(2) != 0) {
                videoPath.setOutputFolder(matcher.group(1).trim());
                String yearString = matcher.group(2);
                if (yearString != null) {
                    videoPath.setYear(yearString);
                }
            }
        }
    }
}
