package net.cserny.videosmover.core.service;

import net.cserny.videosmover.core.helper.StaticPathsProvider;
import net.cserny.videosmover.core.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScanService {

    private final VideoChecker videoChecker;
    private final SubtitlesFinder subtitlesFinder;

    @Autowired
    public ScanService(VideoChecker videoChecker, SubtitlesFinder subtitlesFinder) {
        this.videoChecker = videoChecker;
        this.subtitlesFinder = subtitlesFinder;
    }

    public List<Video> scan(String location) {
        try {
            return Files.walk(StaticPathsProvider.getPath(location), 5, FileVisitOption.FOLLOW_LINKS)
                    .filter(Files::isRegularFile)
                    .filter(videoChecker::isVideo)
                    .map(path -> mapScannedVideos(path, location))
                    .sorted(Comparator.comparing(video -> video.getFullInputPath().toLowerCase()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private Video mapScannedVideos(Path videoPath, String location) {
        Video video = new Video(videoPath.getFileName().toString(), videoPath.toString());
        if (!video.getInputPathWithoutFileName().equals(location)) {
            video.setOutputFolderWithoutDate(videoPath.getParent().getFileName().toString());
        }
        video.setSubtitles(subtitlesFinder.find(videoPath.getParent()));
        return video;
    }
}
