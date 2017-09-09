package service;

import net.cserny.videosMover2.dto.AbstractSimpleFile;
import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.service.VideoMover;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class TestVideoMover implements VideoMover
{
    @Override
    public boolean move(Video video) throws IOException {
        boolean sourceIsReadable = Files.isReadable(video.getInput().getPath());
        boolean targetPathIsWritable = Files.isWritable(video.getOutput().getPath().getParent().getParent());
        boolean subtitlesCanBeMoved = true;

        List<AbstractSimpleFile> subtitles = video.getSubtitles();
        if (subtitles != null && !subtitles.isEmpty()) {
            for (AbstractSimpleFile subtitle : subtitles) {
                boolean subtitleIsReadable = Files.isReadable(subtitle.getPath());
                if (subtitlesCanBeMoved) {
                    subtitlesCanBeMoved = subtitleIsReadable;
                }
            }
        }

        return sourceIsReadable && targetPathIsWritable && subtitlesCanBeMoved;
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
