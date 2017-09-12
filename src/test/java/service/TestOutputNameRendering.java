package service;

import net.cserny.videosMover2.configuration.ServiceConfig;
import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.dto.VideoRow;
import net.cserny.videosMover2.service.OutputNameResolver;
import net.cserny.videosMover2.service.PathsProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class TestOutputNameRendering extends TmpVideoInitializer
{
    @Autowired
    private OutputNameResolver nameResolver;

    @Test
    public void givenTvShowVideoInputWhenParsingShouldReturnTvShowOutput() throws Exception {
        Video video = new Video();
        video.setInput(PathsProvider.getPath(DOWNLOADS_TVSHOW));

        VideoRow videoRow = new VideoRow(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolve(video));

        assertTrue(video.getOutput().startsWith(PathsProvider.getTvShowsPath()));
    }

    @Test
    public void givenMovieVideoInputWhenParsingShouldReturnMovieOutput() throws Exception {
        Video video = new Video();
        video.setInput(PathsProvider.getPath(DOWNLOADS_MOVIE_WITH_SUBTITLE));

        VideoRow videoRow = new VideoRow(video);
        videoRow.setIsMovie(true);
        videoRow.setOutput(nameResolver.resolve(video));

        assertTrue(video.getOutput().startsWith(PathsProvider.getMoviesPath()));
    }

    @Test
    public void givenTvShowVideoInputWhenParsingReturnsCorrectOutput() throws Exception {
        Video video = new Video();
        video.setInput(PathsProvider.getPath(DOWNLOADS_TVSHOW));

        VideoRow videoRow = new VideoRow(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolve(video));

        assertTrue(video.getOutput().getFileName().toString().equals("Game Of Thrones"));
    }

    @Test
    public void givenMovieVideoInputWhenParsingReturnsCorrectOutput() throws Exception {
        Video video = new Video();
        video.setInput(PathsProvider.getPath(DOWNLOADS_MOVIE_WITH_SUBTITLE));

        VideoRow videoRow = new VideoRow(video);
        videoRow.setIsMovie(true);
        videoRow.setOutput(nameResolver.resolve(video));

        assertTrue(video.getOutput().getFileName().toString().equals("The Big Sick (2017)"));
    }

    @Test
    public void givenTvShowWhichAlreadyExistsThenSetOutputToExistingTvShowName() throws Exception {
        Video video = new Video();
        video.setInput(PathsProvider.getPath(DOWNLOADS_EXISTING_TVSHOW));

        VideoRow videoRow = new VideoRow(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolve(video));

        assertTrue(video.getOutput().getFileName().toString().equals("Criminal Minds"));
    }
}
