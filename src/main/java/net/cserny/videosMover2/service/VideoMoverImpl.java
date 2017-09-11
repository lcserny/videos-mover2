package net.cserny.videosMover2.service;

import net.cserny.videosMover2.dto.Video;
import org.springframework.stereotype.Service;

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

        if (!Files.exists(target)) {
            Files.createDirectory(target);
        }

        moveInternal(source, target.resolve(source.getFileName()));
        List<Path> subtitles = video.getSubtitles();
        if (subtitles != null && !subtitles.isEmpty()) {
            for (Path subtitle : subtitles) {
                moveInternal(subtitle, target.resolve(subtitle.getFileName()));
            }
        }

        return Files.exists(target);
    }

    @Override
    public boolean moveAll(List<Video> videoList) throws IOException {
        for (Video video : videoList) {
            if (!move(video)) {
                return false;
            }
        }
        return true;
    }

    private void moveInternal(Path source, Path target) throws IOException {
        if (!Files.exists(target)) {
            Files.move(source, target);
        }
    }
}
