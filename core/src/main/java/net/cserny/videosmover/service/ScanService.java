package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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

    public List<Video> scan(String location) throws IOException {
        List<Path> files = Files.walk(StaticPathsProvider.getPath(location)).filter(Files::isRegularFile).collect(Collectors.toList());
        List<Video> videos = new ArrayList<>();
        for (Path file : files) {
            if (videoChecker.isVideo(file)) {
                Video video = new Video();
                video.setInputPath(file.getParent());
                video.setInputFilename(file.getFileName().toString());
                video.setSubtitles(subtitlesFinder.find(file));
                videos.add(video);
            }
        }
        videos.sort(Comparator.comparing(video -> video.getInputFilename().toLowerCase()));
        return videos;
    }
}
