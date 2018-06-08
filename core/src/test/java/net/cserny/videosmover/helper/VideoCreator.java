package net.cserny.videosmover.helper;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.service.OutputResolver;
import net.cserny.videosmover.service.StaticPathsProvider;

public class VideoCreator {
    public static Video createTvShow(String input, OutputResolver outputResolver) {
        Video video = new Video();
        video.setVideoType(VideoType.TVSHOW);
        video.setInput(StaticPathsProvider.getPath(input));
        video.setOutput(StaticPathsProvider.getPath(outputResolver.resolve(video)));
        return video;
    }

    public static Video createMovie(String input, OutputResolver outputResolver) {
        Video video = new Video();
        video.setVideoType(VideoType.MOVIE);
        video.setInput(StaticPathsProvider.getPath(input));
        video.setOutput(StaticPathsProvider.getPath(outputResolver.resolve(video)));
        return video;
    }
}
