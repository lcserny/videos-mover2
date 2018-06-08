package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoType;
import org.springframework.stereotype.Component;

@Component
public class MovieOutputVideoNameResolver extends AbstractOutputVideoNameResolver {

    @Override
    public boolean canHandle(VideoType type) {
        return type == VideoType.MOVIE;
    }

    @Override
    public String resolve(Video video) {
        String extension = getExtension(video);
        return video.getOutputPath().getFileName().toString() + "." + extension;
    }
}
