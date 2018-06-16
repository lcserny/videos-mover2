package net.cserny.videosmover.helper;

import net.cserny.videosmover.facade.MainFacade;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoPath;
import net.cserny.videosmover.model.VideoType;

import java.nio.file.Path;
import java.util.function.Function;

public class VideoResolver {

    public static Video resolveTvShow(String input, Function<Video, VideoPath> func) {
        return resolve(VideoType.TVSHOW, input, func);
    }

    public static Video resolveMovie(String input, Function<Video, VideoPath> func) {
        return resolve(VideoType.MOVIE, input, func);
    }

    private static Video resolve(VideoType type, String input, Function<Video, VideoPath> func) {
        Video video = new Video();
        video.setVideoType(type);
        setPathsToVideo(video, input, func);
        return video;
    }

    private static void setPathsToVideo(Video video, String input, Function<Video, VideoPath> func) {
        Path inputFullPath = StaticPathsProvider.getPath(input);
        video.setInputPath(inputFullPath.getParent());
        video.setInputFilename(inputFullPath.getFileName().toString());

        VideoPath videoPath = func.apply(video);
        video.setOutputPath(StaticPathsProvider.getPath(videoPath.getOutputPath()));
        video.setOutputFolderName(MainFacade.combineOutputFolderAndYear(videoPath));
    }
}