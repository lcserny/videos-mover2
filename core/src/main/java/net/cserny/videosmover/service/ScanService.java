package net.cserny.videosmover.service;

import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
        List<Video> videos = new ArrayList<>();

        try {
            List<Path> files = Files.walk(StaticPathsProvider.getPath(location)).filter(Files::isRegularFile).collect(Collectors.toList());
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return videos;
    }
}
