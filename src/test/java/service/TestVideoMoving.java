package service;

import net.cserny.videosMover2.configuration.ServiceConfig;
import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.dto.VideoRow;
import net.cserny.videosMover2.service.OutputNameResolver;
import net.cserny.videosMover2.service.PathsProvider;
import net.cserny.videosMover2.service.VideoMover;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class TestVideoMoving extends TmpVideoInitializer
{
    @Autowired
    private OutputNameResolver nameResolver;

    @Autowired
    private VideoMover videoMover;

    @Test
    public void givenVideoRowTvShowWhenMovingThenMoveToTvShowsOutput() throws Exception {
        Video video = new Video();
        video.setInput(PathsProvider.getPath(DOWNLOADS_TVSHOW));

        VideoRow videoRow = new VideoRow(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolve(video));

        assertTrue(videoMover.move(video));
    }

    @Test
    public void givenMultipleVideoRowsTvShowWhenMovingThenMoveAllToTvShowOutput() throws Exception {
        Video video1 = new Video();
        video1.setInput(PathsProvider.getPath(DOWNLOADS_TVSHOW));

        VideoRow videoRow1 = new VideoRow(video1);
        videoRow1.setIsTvShow(true);
        videoRow1.setOutput(nameResolver.resolve(video1));

        Video video2 = new Video();
        video2.setInput(PathsProvider.getPath(DOWNLOADS_EXISTING_TVSHOW));

        VideoRow videoRow2 = new VideoRow(video2);
        videoRow2.setIsMovie(true);
        videoRow2.setOutput(nameResolver.resolve(video2));

        List<Video> videoList = Arrays.asList(videoRow1.getVideo(), videoRow2.getVideo());

        assertTrue(videoMover.moveAll(videoList));
    }

    @Test
    public void givenVideoRowMovieWithSubtitlesWhenMovingThenMoveToMoviesOutputWithSubtitles() throws Exception {
        Video video = new Video();
        video.setInput(PathsProvider.getPath(DOWNLOADS_MOVIE_WITH_SUBTITLE));
        video.setSubtitles(Collections.singletonList(PathsProvider.getPath(DOWNLOADS_SUBTITLE)));

        VideoRow videoRow = new VideoRow(video);
        videoRow.setIsMovie(true);
        videoRow.setOutput(nameResolver.resolve(video));

        assertTrue(videoMover.move(video));
    }

    @Test
    public void whenSubtitlesAreInSubsFolderMoveThemToSubsFolderInOutputAlso() throws Exception {
        Video video = new Video();
        video.setInput(PathsProvider.getPath(DOWNLOADS_MOVIE_WITH_SUBTITLE_IN_SUBS));
        Path subPath1 = PathsProvider.getPath(DOWNLOADS_SUBTITLE_IN_SUBS);
        Path subPath2 = PathsProvider.getPath(DOWNLOADS_SUBTITLE_IN_SUBS_IDX);
        video.setSubtitles(Arrays.asList(subPath1, subPath2));

        VideoRow videoRow = new VideoRow(video);
        videoRow.setIsMovie(true);
        videoRow.setOutput(nameResolver.resolve(video));

        assertTrue(videoMover.move(video));
        Path subtitle1Path = video.getOutput().resolve("Subs").resolve(subPath1.getFileName());
        Path subtitle2Path = video.getOutput().resolve("Subs").resolve(subPath2.getFileName());
        assertTrue(Files.exists(subtitle1Path));
        assertTrue(Files.exists(subtitle2Path));
    }
}
