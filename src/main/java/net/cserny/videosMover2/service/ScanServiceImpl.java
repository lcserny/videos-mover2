package net.cserny.videosMover2.service;

import net.cserny.videosMover2.dto.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leonardo on 02.09.2017.
 */
@Service
public class ScanServiceImpl implements ScanService
{
    private VideoChecker videoChecker;
    private SubtitlesFinder subtitlesFinder;

    @Autowired
    public ScanServiceImpl(VideoChecker videoChecker, SubtitlesFinder subtitlesFinder) {
        this.videoChecker = videoChecker;
        this.subtitlesFinder = subtitlesFinder;
    }

    @Override
    public List<Video> scan(String location) throws IOException {
        List<Video> videos = new ArrayList<>();
        List<Path> files = Files.walk(PathsProvider.getPath(location)).filter(Files::isRegularFile).collect(Collectors.toList());
        for (Path file : files) {
            if (videoChecker.isVideo(file)) {
                Video video = new Video();
                video.setInput(file);
                video.setSubtitles(subtitlesFinder.find(file));
                videos.add(video);
            }
        }
        videos.sort(Comparator.comparing(video -> video.getInput().getFileName().toString().toLowerCase()));
        return videos;
    }
}
