package net.cserny.videosmover.helper;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoType;

import java.nio.file.Path;
import java.util.Collections;

public class VideoResolver {

    public static Video resolveTvShow(String input, ResolvingFunction resolvingFunction) {
        return resolve(VideoType.TVSHOW, input, resolvingFunction);
    }

    public static Video resolveMovie(String input, ResolvingFunction resolvingFunction) {
        return resolve(VideoType.MOVIE, input, resolvingFunction);
    }

    private static Video resolve(VideoType type, String input, ResolvingFunction resolvingFunction) {
        Path inputFullPath = StaticPathsProvider.getPath(input);
        Video video = new Video(inputFullPath.getFileName().toString(), inputFullPath.toString());
        video.setVideoType(type);
        resolvingFunction.process(video, Collections.emptyList());
        return video;
    }
}
