package net.cserny.videosMover2.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.cserny.videosMover2.dto.Video;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leonardo on 02.09.2017.
 */

@Singleton
public class ScanServiceImpl implements ScanService
{
    @Inject
    private VideoChecker videoChecker;
    @Inject
    private SubtitlesFinder subtitlesFinder;

    @Override
    public List<Video> scan(String location) throws IOException {
        List<Video> videos = new ArrayList<>();
        List<Path> files = Files.walk(Paths.get(location)).filter(Files::isRegularFile).collect(Collectors.toList());
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
