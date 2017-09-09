package service;

import net.cserny.videosMover2.configuration.ServiceConfig;
import net.cserny.videosMover2.dto.SimpleFile;
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
// TODO: use a custom File for Video input, output and subtitles list like SimpleFile and generate some before tests
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class OutputNameResolvingTest
{
    @Autowired
    private OutputNameResolver nameResolver;

    @Test
    public void givenNoMovieOutputPathWhenResolvingMovieNameThenShowNothing() throws Exception {
        SystemPathsProvider.setMoviesPath(null);
        Video video = new Video();
        video.setInput(new SimpleFile.Builder(Paths.get(TestVideosProvider.getMovieFilePath())).build());

        String movieOutputPath = nameResolver.resolveMovie(video);

        assertTrue(movieOutputPath.isEmpty());
    }

    @Test
    public void givenTvShowVideoInputWhenParsingShouldReturnTvShowOutput() throws Exception {
        Video video = new Video();
        video.setInput(new SimpleFile.Builder(Paths.get(TestVideosProvider.getTvShowFilePath())).build());

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolveTvShow(video));

        assertTrue(video.getOutput().getPath().startsWith(SystemPathsProvider.getTvShowsPath()));
    }

    @Test
    public void givenMovieVideoInputWhenParsingShouldReturnMovieOutput() throws Exception {
        Video video = new Video();
        video.setInput(new SimpleFile.Builder(Paths.get(TestVideosProvider.getMovieFilePath())).build());

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsMovie(true);
        videoRow.setOutput(nameResolver.resolveMovie(video));

        assertTrue(video.getOutput().getPath().startsWith(SystemPathsProvider.getMoviesPath()));
    }

    @Test
    public void givenTvShowVideoInputWhenParsingReturnsCorrectOutput() throws Exception {
        Video video = new Video();
        video.setInput(new SimpleFile.Builder(Paths.get(TestVideosProvider.getTvShowFilePath())).build());

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolveTvShow(video));

        assertTrue(video.getOutput().getPath().getFileName().toString().equals("Criminal Minds"));
    }

    @Test
    public void givenMovieVideoInputWhenParsingReturnsCorrectOutput() throws Exception {
        Video video = new Video();
        video.setInput(new SimpleFile.Builder(Paths.get(TestVideosProvider.getMovieFilePath())).build());

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsMovie(true);
        videoRow.setOutput(nameResolver.resolveMovie(video));

        assertTrue(video.getOutput().getPath().getFileName().toString().equals("71 (2014)"));
    }

    @Test
    public void givenTvShowWhichAlreadyExistsThenSetOutputToExistingTvShowName() throws Exception {
        Video video = new Video();
        video.setInput(new SimpleFile.Builder(Paths.get(TestVideosProvider.getTvShowFilePath())).build());

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolveTvShow(video));

        assertTrue(video.getOutput().getPath().getFileName().toString().equals("Criminal Minds"));
    }
}
