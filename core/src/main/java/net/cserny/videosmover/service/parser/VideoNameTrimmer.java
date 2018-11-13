package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.helper.PropertiesLoader;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoDate;
import net.cserny.videosmover.service.observer.VideoAdjustmentObserver;
import org.apache.commons.lang3.StringUtils;

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
        String trimmed = trim(video.getOutputFolderName());
        String withoutSpecialChars = stripSpecialChars(trimmed);
        String titleCase = toTitleCase(withoutSpecialChars);
        video.setOutputFolderName(titleCase);
    }

    @Override
    public void parseMovie(Video video, List<VideoAdjustmentObserver> observers) {
        parseTvShow(video, observers);
        appendYear(video);
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

    private void appendYear(Video video) {
        Matcher matcher = videoPattern.matcher(video.getOutputFolderName());
        if (matcher.find()) {
            if (matcher.start(2) != 0) {
                video.setOutputFolderName(matcher.group(1).trim());
                String yearString = matcher.group(2);
                if (!StringUtils.isEmpty(yearString)) {
                    VideoDate videoDate = video.getDate();
                    videoDate.setYear(Integer.valueOf(yearString));
                }
            }
        }
    }
}
