import helper.InMemoryVideoFileSystemInitializer;
import helper.TestHelperConfig;
import helper.VideoCreationHelper;
import net.cserny.videosMover.ApplicationConfig;
import net.cserny.videosMover.model.Video;
import net.cserny.videosMover.service.PathsProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class, TestHelperConfig.class})
public class TestOutputNameRendering extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private VideoCreationHelper videoHelper;

    @Test
    public void givenTvShowVideoInputWhenParsingShouldReturnTvShowOutput() throws Exception {
        Video video = videoHelper.createTvShow(DOWNLOADS_TVSHOW);
        assertTrue(video.getOutput().startsWith(PathsProvider.getTvShowsPath()));
    }

    @Test
    public void givenMovieVideoInputWhenParsingShouldReturnMovieOutput() throws Exception {
        Video video = videoHelper.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE);
        assertTrue(video.getOutput().startsWith(PathsProvider.getMoviesPath()));
    }

    @Test
    public void givenTvShowVideoInputWhenParsingReturnsCorrectOutput() throws Exception {
        Video video = videoHelper.createTvShow(DOWNLOADS_TVSHOW);
        assertTrue(video.getOutput().getFileName().toString().equals("Game Of Thrones"));
    }

    @Test
    public void givenMovieVideoInputWhenParsingReturnsCorrectOutput() throws Exception {
        Video video = videoHelper.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE);
        assertTrue(video.getOutput().getFileName().toString().equals("The Big Sick (2017)"));
    }

    @Test
    public void givenTvShowWhichAlreadyExistsThenSetOutputToExistingTvShowName() throws Exception {
        Video video = videoHelper.createTvShow(DOWNLOADS_EXISTING_TVSHOW);
        assertTrue(video.getOutput().getFileName().toString().equals("Criminal Minds"));
    }
}
