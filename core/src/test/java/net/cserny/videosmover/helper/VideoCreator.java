package net.cserny.videosmover.helper;

import net.cserny.videosmover.facade.MainFacade;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoPath;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.service.OutputResolver;

import java.nio.file.Path;

public class VideoCreator {

    public static Video createTvShow(String input, OutputResolver outputResolver) {
        return create(VideoType.TVSHOW, input, outputResolver);
    }

    public static Video createMovie(String input, OutputResolver outputResolver) {
        return create(VideoType.MOVIE, input, outputResolver);
    }

    private static Video create(VideoType type, String input, OutputResolver resolver) {
        Video video = new Video();
        video.setVideoType(type);
        setPathsToVideo(video, input, resolver);
        return video;
    }

    private static void setPathsToVideo(Video video, String input, OutputResolver resolver) {
        Path inputFullPath = StaticPathsProvider.getPath(input);
        video.setInputPath(inputFullPath.getParent());
        video.setInputFilename(inputFullPath.getFileName().toString());

        VideoPath videoPath = resolver.resolve(video);
        video.setOutputPath(StaticPathsProvider.getPath(videoPath.getOutputPath()));
        video.setOutputFolderName(MainFacade.combineOutputFolderAndYear(videoPath));
    }
}
