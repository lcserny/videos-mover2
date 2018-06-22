package net.cserny.videosmover.helper;

import net.cserny.videosmover.facade.MainFacade;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoPath;
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
        Video video = new Video();
        video.setVideoType(type);
        setPathsToVideo(video, input, resolvingFunction);
        return video;
    }

    private static void setPathsToVideo(Video video, String input, ResolvingFunction resolvingFunction) {
        Path inputFullPath = StaticPathsProvider.getPath(input);
        video.setInputPath(inputFullPath.getParent());
        video.setInputFilename(inputFullPath.getFileName().toString());

        VideoPath videoPath = resolvingFunction.process(video, Collections.emptyList());
        video.setOutputPath(StaticPathsProvider.getPath(videoPath.getOutputPath()));
        video.setOutputFolderName(MainFacade.combineOutputFolderAndYear(videoPath));
    }
}
