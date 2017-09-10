package service;

import net.cserny.videosMover2.configuration.ServiceConfig;
import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.dto.VideoRow;
import net.cserny.videosMover2.service.OutputNameResolver;
import net.cserny.videosMover2.service.SystemPathsProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class OutputNameResolvingTest extends TempVideosInitializer
{
    @Autowired
    private OutputNameResolver nameResolver;

    @Test
    public void givenNoMovieOutputPathWhenResolvingMovieNameThenShowNothing() throws Exception {
        SystemPathsProvider.setMoviesPath(null);
        Video video = new Video();
        video.setInput(Paths.get(TestVideosProvider.getMovieFilePath()));

        String movieOutputPath = nameResolver.resolveMovie(video);

        assertTrue(movieOutputPath.isEmpty());
    }

    @Test
    public void givenTvShowVideoInputWhenParsingShouldReturnTvShowOutput() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get(TestVideosProvider.getTvShowFilePath()));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolveTvShow(video));

        assertTrue(video.getOutput().startsWith(SystemPathsProvider.getTvShowsPath()));
    }

    @Test
    public void givenMovieVideoInputWhenParsingShouldReturnMovieOutput() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get(TestVideosProvider.getMovieFilePath()));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsMovie(true);
        videoRow.setOutput(nameResolver.resolveMovie(video));

        assertTrue(video.getOutput().startsWith(SystemPathsProvider.getMoviesPath()));
    }

    @Test
    public void givenTvShowVideoInputWhenParsingReturnsCorrectOutput() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get(TestVideosProvider.getTvShowFilePath()));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolveTvShow(video));

        assertTrue(video.getOutput().getFileName().toString().equals("Criminal Minds"));
    }

    @Test
    public void givenMovieVideoInputWhenParsingReturnsCorrectOutput() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get(TestVideosProvider.getMovieFilePath()));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsMovie(true);
        videoRow.setOutput(nameResolver.resolveMovie(video));

        assertTrue(video.getOutput().getFileName().toString().equals("71 (2014)"));
    }

    @Test
    public void givenTvShowWhichAlreadyExistsThenSetOutputToExistingTvShowName() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get(TestVideosProvider.getTvShowFilePath()));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolveTvShow(video));

        assertTrue(video.getOutput().getFileName().toString().equals("Criminal Minds"));
    }
}
