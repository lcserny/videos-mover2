package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.helper.PropertiesLoader;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.observer.VideoAdjustmentObserver;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Singleton
public class VideoNameTrimmer implements VideoNameParser {

    private final Pattern videoPattern = Pattern.compile("(.*)(\\d{4})");
    private List<Pattern> nameTrimPartPatterns;

    @Inject
    public VideoNameTrimmer() {
        nameTrimPartPatterns = PropertiesLoader.getNameTrimParts().stream()
                .filter(part -> !part.isEmpty())
                .map(part -> Pattern.compile("(?i)(-?" + part + ")"))
                .collect(Collectors.toList());
    }

    @Override
    public void parseTvShow(Video video, List<VideoAdjustmentObserver> observers) {
        String trimmed = trim(videoPath.getOutputFolder());
        String withoutExtension = removeExtension(trimmed);
        String camelCase = toCamelCase(withoutExtension);
        videoPath.setOutputFolder(camelCase);
    }

    @Override
    public void parseMovie(Video video, List<VideoAdjustmentObserver> observers) {
        parseTvShow(videoPath, observers);
        appendYear(videoPath);
    }

    private String trim(String filename) {
        for (Pattern part : nameTrimPartPatterns) {
            Matcher matcher = part.matcher(filename);
            if (matcher.find()) {
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
        boolean extensionFirstLetter = isExtensionLetterOrDigit(text.charAt(text.length() - 3));
        boolean extensionSecondLetter = isExtensionLetterOrDigit(text.charAt(text.length() - 2));
        boolean extensionThirdLetter = isExtensionLetterOrDigit(text.charAt(text.length() - 1));

        if (extensionPeriodExists && extensionFirstLetter && extensionSecondLetter && extensionThirdLetter) {
            return text.substring(0, text.length() - 4);
        }
        return text;
    }

    private boolean isExtensionLetterOrDigit(char a) {
        boolean letter = Character.isLetter(a);
        boolean digit = Character.isDigit(a);

        return (letter && Character.isLowerCase(a)) || digit;
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
