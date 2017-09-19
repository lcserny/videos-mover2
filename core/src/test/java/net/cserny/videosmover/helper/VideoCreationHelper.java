package net.cserny.videosmover.helper;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.OutputNameResolver;
import net.cserny.videosmover.service.PathsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VideoCreationHelper {
    private final OutputNameResolver nameResolver;

    @Autowired
    public VideoCreationHelper(OutputNameResolver nameResolver) {
        this.nameResolver = nameResolver;
    }

    public Video createTvShow(String input) {
        Video video = new Video();
        video.setIsTvShow(true);
        video.setInput(PathsProvider.getPath(input));
        video.setOutput(PathsProvider.getPath(nameResolver.resolve(video)));
        return video;
    }

    public Video createMovie(String input) {
        Video video = new Video();
        video.setIsMovie(true);
        video.setInput(PathsProvider.getPath(input));
        video.setOutput(PathsProvider.getPath(nameResolver.resolve(video)));
        return video;
    }
}
