package net.cserny.videosmover.core.helper;

import net.cserny.videosmover.core.model.Video;
import net.cserny.videosmover.core.model.VideoType;

import java.nio.file.Path;
import java.util.Collections;

public class VideoResolver {

    public static Video resolveTvShow(String location, String input, ResolvingFunction resolvingFunction) {
        return resolve(VideoType.TVSHOW, input, location, resolvingFunction);
    }

    public static Video resolveMovie(String location, String input, ResolvingFunction resolvingFunction) {
        return resolve(VideoType.MOVIE, input, location, resolvingFunction);
    }

    private static Video resolve(VideoType type, String input, String location, ResolvingFunction resolvingFunction) {
        Path inputFullPath = StaticPathsProvider.getPath(input);
        Video video = new Video(inputFullPath.getFileName().toString(), inputFullPath.toString());
        video.setVideoType(type);
        if (!video.getInputPathWithoutFileName().equals(location)) {
            video.setOutputFolderWithoutDate(inputFullPath.getParent().getFileName().toString());
        }
        resolvingFunction.process(video, Collections.emptyList());
        return video;
    }
}
