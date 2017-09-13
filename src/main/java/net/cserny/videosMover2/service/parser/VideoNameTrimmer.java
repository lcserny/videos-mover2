package net.cserny.videosMover2.service.parser;

import net.cserny.videosMover2.service.AbstractResourceInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leonardo on 10.09.2017.
 */
@Service
@Order(1)
public class VideoNameTrimmer extends AbstractResourceInitializer implements VideoNameParser
{
    public static final String RESOURCE_NAME_PARTS = "name_parts.cfg";

    private Pattern videoPattern = Pattern.compile("(.*)(\\d{4})");
    private List<String> nameTrimParts;

    public VideoNameTrimmer() {
        this.nameTrimParts = fillListFromResource(RESOURCE_NAME_PARTS);
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
                filename = filename.substring(0, matcher.start());
            }
        }
        return filename;
    }

    private String toCamelCase(String text) {
        StringBuilder camelCaseString = new StringBuilder();
        for (String part : stripSpecialChars(text).split("\\s+")) {
            camelCaseString.append(toProperCase(part)).append(" ");
        }
        return camelCaseString.toString().trim();
    }

    private String removeExtension(String text) {
        if (text.charAt(text.length() - 4) == '.') {
            return text.substring(0, text.length() - 4);
        }
        return text;
    }

    private String stripSpecialChars(String videoName) {
        return videoName.replaceAll("([._])", " ").trim();
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
