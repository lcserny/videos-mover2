package service;

import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.dto.VideoRow;
import net.cserny.videosMover2.service.OutputNameResolver;
import net.cserny.videosMover2.service.VideoMover;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
public class MovingTest
{
    private List<Video> restoreVideos = new ArrayList<>();

    private OutputNameResolver nameResolver;
    private VideoMover videoMover;

    @Autowired
    public MovingTest(OutputNameResolver nameResolver, VideoMover videoMover) {
        this.nameResolver = nameResolver;
        this.videoMover = videoMover;
    }

    @After
    public void tearDown() throws Exception {
        for (Video video : restoreVideos) {
            Files.move(video.getOutput(), video.getInput());

            List<Path> subtitles = video.getSubtitles();
            if (subtitles != null && !subtitles.isEmpty()) {
                for (Path subtitle : subtitles) {
                    Path subtitleSource = video.getOutput().getParent().resolve(subtitle.getFileName());
                    Path subtitleTarget = video.getInput().getParent().resolve(subtitle.getFileName());
                    Files.move(subtitleSource, subtitleTarget);
                }
            }
        }
    }

    @Test
    public void givenVideoRowTvShowWhenMovingThenMoveToTvShowsOutput() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get("/mnt/Data/Downloads/www.Torrenting.com - Criminal.Minds.S12E22.HDTV.x264-SVA/Crimil.Minds.S12E22.HDTV.x264-SVA.mkv"));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolveTvShow(video));

        assertTrue(videoMover.move(video));
        assertTrue(Files.exists(video.getOutput()));

        restoreVideos.add(videoRow.getVideo());
    }

    @Test
    public void givenMultipleVideoRowsTvShowWhenMovingThenMoveAllToTvShowOutput() throws Exception {
        Video video1 = new Video();
        video1.setInput(Paths.get("/mnt/Data/Downloads/www.Torrenting.com - Criminal.Minds.S12E22.HDTV.x264-SVA/Crimil.Minds.S12E22.HDTV.x264-SVA.mkv"));

        VideoRow videoRow1 = new VideoRow();
        videoRow1.setVideo(video1);
        videoRow1.setIsTvShow(true);
        videoRow1.setOutput(nameResolver.resolveTvShow(video1));

        Video video2 = new Video();
        video2.setInput(Paths.get("/mnt/Data/Downloads/www.Torrenting.com - Criminal.Minds.S12E22.HDTV.x264-SVA/Crimil.Minds.S12E22.HDTV.x264-SVA.mkv"));

        VideoRow videoRow2 = new VideoRow();
        videoRow2.setVideo(video2);
        videoRow2.setIsTvShow(true);
        videoRow2.setOutput(nameResolver.resolveTvShow(video2));

        List<Video> videoList = Arrays.asList(videoRow1.getVideo(), videoRow2.getVideo());

        assertTrue(videoMover.moveAll(videoList));
        for (Video video : videoList) {
            assertTrue(Files.exists(video.getOutput()));
        }

        restoreVideos.addAll(videoList);
    }

    @Test
    public void givenVideoRowMovieWithSubtitlesWhenMovingThenMoveToMoviesOutputWithSubtitles() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get("/mnt/Data/Downloads/71 (2014) [1080p]/71.2014.1080p.BluRay.x264.YIFY.mkv"));
        video.setSubtitles(Collections.singletonList(Paths.get("/mnt/Data/Downloads/71 (2014) [1080p]/71.2014.1080p.BluRay.x264.YIFY.srt")));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsMovie(true);
        videoRow.setOutput(nameResolver.resolveMovie(video));

        assertTrue(videoMover.move(video));
        assertTrue(Files.exists(video.getOutput()));
        for (Path subtitle : video.getSubtitles()) {
            assertTrue(Files.exists(video.getOutput().getParent().resolve(subtitle.getFileName())));
        }

        restoreVideos.add(videoRow.getVideo());
    }
}
