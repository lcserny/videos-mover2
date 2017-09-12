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
    private static final String SUBTITLE_SUBPATH = "Subs";

    @Override
    public boolean move(Video video) throws IOException {
        Path source = video.getInput();
        Path target = video.getOutput();

        createDirectoryInternal(target);
        moveInternal(source, target.resolve(source.getFileName()));

        List<Path> subtitles = video.getSubtitles();
        if (subtitles != null && !subtitles.isEmpty()) {
            for (Path subtitle : subtitles) {
                Path partialTarget = getPartialSubtitleTarget(target, subtitle);
                moveInternal(subtitle, partialTarget.resolve(subtitle.getFileName()));
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

    private Path getPartialSubtitleTarget(Path target, Path subtitle) throws IOException {
        Path partialTarget = target;
        if (subtitle.getParent().getFileName().toString().equals(SUBTITLE_SUBPATH)) {
            partialTarget = target.resolve(SUBTITLE_SUBPATH);
            createDirectoryInternal(partialTarget);
        }
        return partialTarget;
    }

    private void createDirectoryInternal(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            Files.createDirectory(directory);
        }
    }

    private void moveInternal(Path source, Path target) throws IOException {
        if (!Files.exists(target)) {
            Files.move(source, target);
        }
    }
}
