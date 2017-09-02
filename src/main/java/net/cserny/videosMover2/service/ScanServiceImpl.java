package net.cserny.videosMover2.service;

import net.cserny.videosMover2.dto.Video;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leonardo on 02.09.2017.
 */
public class ScanServiceImpl implements ScanService
{
    private VideoParser videoParser;

    public ScanServiceImpl(VideoParser videoParser) {
        this.videoParser = videoParser;
    }

    @Override
    public List<Video> scan(String location) throws IOException {
        List<Video> videos = new ArrayList<>();
        List<Path> files = Files.walk(Paths.get(location)).filter(Files::isRegularFile).collect(Collectors.toList());
        for (Path file : files) {
            if (videoParser.isVideo(file)) {
                Video video = new Video();
                video.setFile(file);
                videos.add(video);
            }
        }
        return videos;
    }
}
