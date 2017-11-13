package net.cserny.videosmover.service;

import net.cserny.videosmover.ApplicationConfig;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.model.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
public class VideoMoverTest extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private ServiceFacade serviceFacade;
    @Autowired
    private OutputResolver outputResolver;

    @Test
    public void move_multiTvShowsToCorrectPath() throws Exception {
        Video video1 = serviceFacade.createTvShow(DOWNLOADS_TVSHOW);
        Video video2 = serviceFacade.createTvShow(DOWNLOADS_EXISTING_TVSHOW);
        List<Video> videoList = Arrays.asList(video1, video2);
        serviceFacade.assertMovingVideos(videoList, StaticPathsProvider.getPath(StaticPathsProvider.getTvShowsPath()));
    }

    @Test
    public void move_movieWithSubtitleToCorrectPathRetainingSubtitle() throws Exception {
        Video video = serviceFacade.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE);
        Path subtitlePath = StaticPathsProvider.getPath(DOWNLOADS_SUBTITLE);
        video.setSubtitles(Collections.singletonList(subtitlePath));
        serviceFacade.assertMovingVideos(Collections.singletonList(video), StaticPathsProvider.getPath(StaticPathsProvider.getMoviesPath()));
    }

    @Test
    public void move_subtitlesInSubsFolderThemToSubsFolderInOutputAlso() throws Exception {
        Video video = serviceFacade.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE_IN_SUBS);
        Path subPath1 = StaticPathsProvider.getPath(DOWNLOADS_SUBTITLE_IN_SUBS);
        Path subPath2 = StaticPathsProvider.getPath(DOWNLOADS_SUBTITLE_IN_SUBS_IDX);
        video.setSubtitles(Arrays.asList(subPath1, subPath2));

        Path movedPath = StaticPathsProvider.getPath(StaticPathsProvider.getMoviesPath());
        serviceFacade.assertMovingVideos(Collections.singletonList(video), movedPath);
        serviceFacade.assertMovingSubtitles(video, movedPath, true);
    }
}
