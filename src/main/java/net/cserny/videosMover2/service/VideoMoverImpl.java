package net.cserny.videosMover2.service;

import net.cserny.videosMover2.dto.Video;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by leonardo on 03.09.2017.
 */
public class VideoMoverImpl implements VideoMover
{
    @Override
    public boolean move(Video video) throws IOException {
        Path source = video.getInput();
        Path target = video.getOutput();

        if (!Files.exists(target.getParent())) {
            Files.createDirectory(target.getParent());
        }

        Files.move(source, target);
        List<Path> subtitles = video.getSubtitles();
        if (subtitles != null && !subtitles.isEmpty()) {
            for (Path subtitle : subtitles) {
                Files.move(subtitle, target.getParent().resolve(subtitle.getFileName()));
            }
        }

        return Files.exists(target);
    }

    @Override
    public boolean moveAll(List<Video> videoList) throws IOException {
        boolean finalResult = true;
        for (Video video : videoList) {
            boolean videoResult = move(video);
            if (!videoResult) {
                finalResult = false;
            }
        }
        return finalResult;
    }
}
