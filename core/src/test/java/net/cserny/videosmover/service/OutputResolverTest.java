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
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.containsString;
//import static org.junit.Assert.assertTrue;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ApplicationConfig.class)
//public class OutputResolverTest extends InMemoryVideoFileSystemInitializer {
//    @Autowired
//    private OutputResolver outputResolver;
//
//    @Test
//    public void resolve_allDigitsMovieResolvesNameCorrectly() throws Exception {
//        Video video = VideoCreator.createMovie(DOWNLOADS_MOVIE_ALL_DIGITS, outputResolver);
//        assertThat(video.getOutput().getFileName().toString(), containsString("1922 (2017)"));
//    }
//
//    @Test
//    public void resolve_tvShowInputReturnsCorrectOutput() throws Exception {
//        Video video = VideoCreator.createTvShow(DOWNLOADS_TVSHOW, outputResolver);
//        assertTrue(video.getOutput().startsWith(StaticPathsProvider.getTvShowsPath()));
//        assertTrue(video.getOutput().getFileName().toString().equals("Game Of Thrones"));
//    }
//
//    @Test
//    public void resolve_movieInputReturnsCorrectOutput() throws Exception {
//        Video video = VideoCreator.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE, outputResolver);
//        assertTrue(video.getOutput().startsWith(StaticPathsProvider.getMoviesPath()));
//        assertTrue(video.getOutput().getFileName().toString().equals("The Big Sick (2017)"));
//    }
//
//    @Test
//    public void resolve_tvShowInputSetsOutputToExistingTvShowName() throws Exception {
//        Video video = VideoCreator.createTvShow(DOWNLOADS_EXISTING_TVSHOW, outputResolver);
//        assertTrue(video.getOutput().getFileName().toString().equals("Criminal Minds"));
//    }
//}