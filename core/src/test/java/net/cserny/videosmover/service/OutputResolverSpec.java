package net.cserny.videosmover.service;

import net.cserny.videosmover.ApplicationConfig;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.helper.VideoCreationHelper;
import net.cserny.videosmover.model.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class OutputResolverSpec extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private VideoCreationHelper videoHelper;

    @Test
    public void allDigitsMovieShouldResolvNameCorrectly() throws Exception {
        Video video = videoHelper.createMovie(DOWNLOADS_MOVIE_ALL_DIGITS);
        assertThat(video.getOutput().getFileName().toString(), containsString("1922 (2017)"));
    }

    @Test
    public void tvShowInputReturnsTvShowOutputPath() throws Exception {
        Video video = videoHelper.createTvShow(DOWNLOADS_TVSHOW);
        assertTrue(video.getOutput().startsWith(StaticPathsProvider.getTvShowsPath()));
    }

    @Test
    public void movieInputReturnsMovieOutputPath() throws Exception {
        Video video = videoHelper.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE);
        assertTrue(video.getOutput().startsWith(StaticPathsProvider.getMoviesPath()));
    }

    @Test
    public void tvShowInputReturnsCorrectOutput() throws Exception {
        Video video = videoHelper.createTvShow(DOWNLOADS_TVSHOW);
        assertTrue(video.getOutput().getFileName().toString().equals("Game Of Thrones"));
    }

    @Test
    public void movieInputReturnsCorrectOutput() throws Exception {
        Video video = videoHelper.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE);
        assertTrue(video.getOutput().getFileName().toString().equals("The Big Sick (2017)"));
    }

    @Test
    public void tvShowInputSetsOutputToExistingTvShowName() throws Exception {
        Video video = videoHelper.createTvShow(DOWNLOADS_EXISTING_TVSHOW);
        assertTrue(video.getOutput().getFileName().toString().equals("Criminal Minds"));
    }
}