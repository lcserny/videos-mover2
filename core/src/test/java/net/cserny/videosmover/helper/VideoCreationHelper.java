package net.cserny.videosmover.helper;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.OutputResolver;
import net.cserny.videosmover.service.StaticPathsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VideoCreationHelper {
    private final OutputResolver outputResolver;

    @Autowired
    public VideoCreationHelper(OutputResolver outputResolver) {
        this.outputResolver = outputResolver;
    }

    public Video createTvShow(String input) {
        Video video = new Video();
        video.setIsTvShow(true);
        video.setInput(StaticPathsProvider.getPath(input));
        video.setOutput(StaticPathsProvider.getPath(outputResolver.resolve(video)));
        return video;
    }

    public Video createMovie(String input) {
        Video video = new Video();
        video.setIsMovie(true);
        video.setInput(StaticPathsProvider.getPath(input));
        video.setOutput(StaticPathsProvider.getPath(outputResolver.resolve(video)));
        return video;
    }
}
