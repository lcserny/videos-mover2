package service;

import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.dto.VideoRow;
import net.cserny.videosMover2.service.VideoMover;
import net.cserny.videosMover2.service.VideoMoverImpl;
import net.cserny.videosMover2.service.VideoOutputNameResolver;
import net.cserny.videosMover2.service.VideoOutputNameResolverImpl;
import org.junit.After;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
public class MovingTest
{
    private VideoOutputNameResolver nameResolver = new VideoOutputNameResolverImpl();
    private VideoMover videoMover = new VideoMoverImpl();
    private List<VideoRow> restoreVideos = new ArrayList<>();

    // TODO: uncomment as moving actually happens
//    @After
//    public void tearDown() throws Exception {
//        for (VideoRow videoRow : restoreVideos) {
//            Video video = videoRow.getVideo();
//            Path input = video.getInput();
//            Path output = video.getOutput();
//            Files.move(output, input);
//
//            List<Path> subtitles = video.getSubtitles();
//            if (subtitles != null && !subtitles.isEmpty()) {
//                for (Path subtitle : subtitles) {
//                    Files.move(subtitle, video.getInput().getParent().resolve(subtitle.getFileName()));
//                }
//            }
//        }
//    }

    @Test
    public void givenVideoRowTvShowWhenMovingThenMoveToTvShowsOutput() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get("/mnt/Data/Downloads/www.Torrenting.com - Criminal.Minds.S12E22.HDTV.x264-SVA/Crimil.Minds.S12E22.HDTV.x264-SVA.mkv"));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolveTvShow(video));

        assertTrue(videoMover.move(video));

        restoreVideos.add(videoRow);
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

        List<VideoRow> videoRowList = Arrays.asList(videoRow1, videoRow2);

        assertTrue(videoMover.moveAll(videoRowList));

        restoreVideos.addAll(videoRowList);
    }

//    @Test
//    public void givenVideoRowMovieWithSubtitlesWhenMovingThennMoveToMoviesOutputWithSubtitles() throws Exception {
//
//    }
}
