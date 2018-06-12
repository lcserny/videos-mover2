package net.cserny.videosmover.helper;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.service.OutputResolver;

import java.nio.file.Path;

public class VideoCreator {

    public static Video createTvShow(String input, OutputResolver outputResolver) {
        Video video = new Video();
        video.setVideoType(VideoType.TVSHOW);
        setPathsToVideo(video, input, outputResolver);
        return video;
    }

    public static Video createMovie(String input, OutputResolver outputResolver) {
        Video video = new Video();
        video.setVideoType(VideoType.MOVIE);
        setPathsToVideo(video, input, outputResolver);
        return video;
    }

    private static void setPathsToVideo(Video video, String input, OutputResolver resolver) {
        Path inputFullPath = StaticPathsProvider.getPath(input);
        video.setInputPath(inputFullPath.getParent());
        video.setInputFilename(inputFullPath.getFileName().toString());

        Path outputFullPath = StaticPathsProvider.getPath(resolver.resolve(video));
        video.setOutputPath(outputFullPath.getParent());
        video.setOutputFilename(outputFullPath.getFileName().toString());
    }
}
