package net.cserny.videosMover2.service;

import net.cserny.videosMover2.dto.AbstractSimpleFile;
import net.cserny.videosMover2.dto.Video;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by leonardo on 03.09.2017.
 */
@Service
public class VideoMoverImpl implements VideoMover
{
    @Override
    public boolean move(Video video) throws IOException {
        Path source = video.getInput().getPath();
        Path target = video.getOutput().getPath();

        if (!Files.exists(target.getParent())) {
            Files.createDirectory(target.getParent());
        }

        Files.move(source, target);
        List<AbstractSimpleFile> subtitles = video.getSubtitles();
        if (subtitles != null && !subtitles.isEmpty()) {
            for (AbstractSimpleFile subtitle : subtitles) {
                Files.move(subtitle.getPath(), target.getParent().resolve(subtitle.getPath().getFileName()));
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
