package helper;

import net.cserny.videosMover.model.Video;
import net.cserny.videosMover.service.OutputNameResolver;
import net.cserny.videosMover.service.PathsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VideoCreationHelper
{
    @Autowired
    private OutputNameResolver nameResolver;

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
