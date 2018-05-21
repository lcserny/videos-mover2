//package net.cserny.videosmover.service;
//
//import net.cserny.videosmover.ApplicationConfig;
//import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
//import net.cserny.videosmover.helper.VideoCreator;
//import net.cserny.videosmover.model.Video;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.Assert.assertTrue;
//
///**
// * Created by leonardo on 02.09.2017.
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ApplicationConfig.class)
//public class VideoMoverTest extends InMemoryVideoFileSystemInitializer {
//    @Autowired
//    private VideoMover videoMover;
//    @Autowired
//    private OutputResolver outputResolver;
//
//    @Test
//    public void move_multiTvShowsToCorrectPath() throws Exception {
//        Video video1 = VideoCreator.createTvShow(DOWNLOADS_TVSHOW, outputResolver);
//        Video video2 = VideoCreator.createTvShow(DOWNLOADS_EXISTING_TVSHOW, outputResolver);
//        List<Video> videoList = Arrays.asList(video1, video2);
//
//        assertTrue(videoMover.move(videoList));
//        Path movedTvShowPath1 = StaticPathsProvider.getPath(StaticPathsProvider.getTvShowsPath()).resolve(video1.getOutput());
//        assertTrue(Files.exists(movedTvShowPath1));
//        Path movedTvShowPath2 = StaticPathsProvider.getPath(StaticPathsProvider.getTvShowsPath()).resolve(video2.getOutput());
//        assertTrue(Files.exists(movedTvShowPath2));
//    }
//
//    @Test
//    public void move_movieWithSubtitleToCorrectPathRetainingSubtitle() throws Exception {
//        Video video = VideoCreator.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE, outputResolver);
//        Path subtitlePath = StaticPathsProvider.getPath(DOWNLOADS_SUBTITLE);
//        video.setSubtitles(Collections.singletonList(subtitlePath));
//
//        assertTrue(videoMover.move(video));
//        Path movedMoviePath = StaticPathsProvider.getPath(StaticPathsProvider.getMoviesPath()).resolve(video.getOutput());
//        assertTrue(Files.exists(movedMoviePath));
//        Path movedMovieSubtitlePath = movedMoviePath.resolve(subtitlePath.getFileName());
//        assertTrue(Files.exists(movedMovieSubtitlePath));
//    }
//
//    @Test
//    public void move_subtitlesInSubsFolderThemToSubsFolderInOutputAlso() throws Exception {
//        Video video = VideoCreator.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE_IN_SUBS, outputResolver);
//        Path subPath1 = StaticPathsProvider.getPath(DOWNLOADS_SUBTITLE_IN_SUBS);
//        Path subPath2 = StaticPathsProvider.getPath(DOWNLOADS_SUBTITLE_IN_SUBS_IDX);
//        video.setSubtitles(Arrays.asList(subPath1, subPath2));
//
//        assertTrue(videoMover.move(video));
//        assertTrue(Files.exists(video.getOutput().resolve("Subs").resolve(subPath1.getFileName())));
//        assertTrue(Files.exists(video.getOutput().resolve("Subs").resolve(subPath2.getFileName())));
//    }
//}
