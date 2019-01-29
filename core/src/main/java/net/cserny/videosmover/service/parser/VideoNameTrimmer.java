package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.helper.PropertiesLoader;
import net.cserny.videosmover.helper.StringHelper;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.observer.VideoAdjustmentObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Order(0)
@Component
public class VideoNameTrimmer implements VideoNameParser {

    private final Pattern videoPattern = Pattern.compile("(.*)(\\d{4})");
    private List<Pattern> nameTrimPartPatterns;

    @Autowired
    public VideoNameTrimmer() {
        nameTrimPartPatterns = PropertiesLoader.getNameTrimParts().stream()
                .filter(part -> !part.isEmpty())
                .map(part -> Pattern.compile("(?i)(-?" + part + ")"))
                .collect(Collectors.toList());
    }

    @Override
    public void parseTvShow(Video video, List<VideoAdjustmentObserver> observers) {
        String trimmed = trim(video.getOutputFolderWithoutDate());
        String withoutSpecialChars = stripSpecialChars(trimmed);
        String titleCase = toTitleCase(withoutSpecialChars);
        video.setOutputFolderWithoutDate(titleCase);
    }

    @Override
    public void parseMovie(Video video, List<VideoAdjustmentObserver> observers) {
        parseTvShow(video, observers);
        resolveYear(video);
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

    private String toTitleCase(String name) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        for (char c : name.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }

    private String stripSpecialChars(String videoName) {
        return videoName.replaceAll("([\\[._\\]])", " ").trim();
    }

    private void resolveYear(Video video) {
        Matcher matcher = videoPattern.matcher(video.getOutputFolderWithoutDate());
        if (matcher.find()) {
            if (matcher.start(2) != 0) {
                video.setOutputFolderWithoutDate(matcher.group(1).trim());
                String yearString = matcher.group(2);
                if (!StringHelper.isEmpty(yearString)) {
                    video.setYear(Integer.valueOf(yearString));
                }
            }
        }
    }
}
