package service;

import net.cserny.videosMover2.configuration.ServiceConfig;
import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.dto.VideoRow;
import net.cserny.videosMover2.service.OutputNameResolver;
import net.cserny.videosMover2.service.VideoMover;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class MovingTest extends TempVideoInitializer
{
    @Autowired
    private OutputNameResolver nameResolver;

    @Autowired
    private VideoMover videoMover;

    @Test
    public void givenVideoRowTvShowWhenMovingThenMoveToTvShowsOutput() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get(DOWNLOADS_TVSHOW));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolveTvShow(video));

        assertTrue(videoMover.move(video));
    }

    @Test
    public void givenMultipleVideoRowsTvShowWhenMovingThenMoveAllToTvShowOutput() throws Exception {
        Video video1 = new Video();
        video1.setInput(Paths.get(DOWNLOADS_TVSHOW));

        VideoRow videoRow1 = new VideoRow();
        videoRow1.setVideo(video1);
        videoRow1.setIsTvShow(true);
        videoRow1.setOutput(nameResolver.resolveTvShow(video1));

        Video video2 = new Video();
        video2.setInput(Paths.get(DOWNLOADS_EXISTING_TVSHOW));

        VideoRow videoRow2 = new VideoRow();
        videoRow2.setVideo(video2);
        videoRow2.setIsMovie(true);
        videoRow2.setOutput(nameResolver.resolveTvShow(video2));

        List<Video> videoList = Arrays.asList(videoRow1.getVideo(), videoRow2.getVideo());

        assertTrue(videoMover.moveAll(videoList));
    }

    @Test
    public void givenVideoRowMovieWithSubtitlesWhenMovingThenMoveToMoviesOutputWithSubtitles() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get(DOWNLOADS_MOVIE_WITH_SUBTITLE));
        video.setSubtitles(Collections.singletonList(Paths.get(DOWNLOADS_SUBTITLE)));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsMovie(true);
        videoRow.setOutput(nameResolver.resolveMovie(video));

        assertTrue(videoMover.move(video));
    }
}
