package net.cserny.videosmover.service;

import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ScanService {

    private final VideoChecker videoChecker;
    private final SubtitlesFinder subtitlesFinder;

    @Inject
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
                    .sorted(Comparator.comparing(video -> video.getInputPath().toString().toLowerCase()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private Video mapScannedVideos(Path videoPath, String location) {
        Video video = new Video();
        video.setFileName(videoPath.getFileName().toString());

        video.setInputPath(videoPath.getParent().toString());
        if (!video.getInputPath().equals(location)) {
            video.setInputFolderName(videoPath.getParent().getFileName().toString());
        }

        video.setSubtitles(subtitlesFinder.find(videoPath.getParent()));
        return video;
    }
}
