package net.cserny.videosmover.helper;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.OutputResolver;
import net.cserny.videosmover.service.PathsProvider;
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
        video.setInput(PathsProvider.getPath(input));
        video.setOutput(PathsProvider.getPath(outputResolver.resolve(video).getOutput()));
        return video;
    }

    public Video createMovie(String input) {
        Video video = new Video();
        video.setIsMovie(true);
        video.setInput(PathsProvider.getPath(input));
        video.setOutput(PathsProvider.getPath(outputResolver.resolve(video).getOutput()));
        return video;
    }
}
