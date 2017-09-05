package service;

import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.dto.VideoRow;
import net.cserny.videosMover2.service.OutputNameResolver;
import net.cserny.videosMover2.service.SystemPathsProvider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
public class OutputNameResolvingTest
{
    @Autowired
    private OutputNameResolver nameResolver;

    @Test
    public void givenNoMovieOutputPathWhenResolvingMovieNameThenShowNothing() throws Exception {
        SystemPathsProvider.setMoviesPath(null);
        Video video = new Video();
        video.setInput(Paths.get("/mnt/Data/Downloads/71 (2014) [1080p]/71.2014.1080p.BluRay.x264.YIFY.mkv"));

        String movieOutputPath = nameResolver.resolveMovie(video);

        assertTrue(movieOutputPath.isEmpty());
    }

    @Test
    public void givenTvShowVideoInputWhenParsingShouldReturnTvShowOutput() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get("/mnt/Data/Downloads/[ www.torrenting.com ] - Criminal.Minds.S12E01.HDTV.x264-FLEET/Criminal.Minds.S12E01.HDTV.x264-FLEET.mkv"));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolveTvShow(video));

        assertTrue(video.getOutput().startsWith(SystemPathsProvider.getTvShowsPath()));
    }

    @Test
    public void givenMovieVideoInputWhenParsingShouldReturnMovieOutput() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get("/mnt/Data/Downloads/71 (2014) [1080p]/71.2014.1080p.BluRay.x264.YIFY.mkv"));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsMovie(true);
        videoRow.setOutput(nameResolver.resolveMovie(video));

        assertTrue(video.getOutput().startsWith(SystemPathsProvider.getMoviesPath()));
    }

    @Test
    public void givenTvShowVideoInputWhenParsingReturnsCorrectOutput() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get("/mnt/Data/Downloads/[ www.torrenting.com ] - Criminal.Minds.S12E01.HDTV.x264-FLEET/Criminal.Minds.S12E01.HDTV.x264-FLEET.mkv"));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolveTvShow(video));

        assertTrue(video.getOutput().getFileName().toString().equals("Criminal Minds"));
    }

    @Test
    public void givenMovieVideoInputWhenParsingReturnsCorrectOutput() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get("/mnt/Data/Videos/Movies/A Cure For Wellness (2016)/A.Cure.for.Wellness.2016.BDRip.x264-DRONES.mkv"));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsMovie(true);
        videoRow.setOutput(nameResolver.resolveMovie(video));

        assertTrue(video.getOutput().getFileName().toString().equals("A Cure For Wellness (2016)"));
    }

    @Test
    public void givenTvShowWhichAlreadyExistsThenSetOutputToExistingTvShowName() throws Exception {
        Video video = new Video();
        video.setInput(Paths.get("/mnt/Data/Downloads/www.Torrenting.com - Criminal.Minds.S12E22.HDTV.x264-SVA/Crimil.Minds.S12E22.HDTV.x264-SVA.mkv"));

        VideoRow videoRow = new VideoRow();
        videoRow.setVideo(video);
        videoRow.setIsTvShow(true);
        videoRow.setOutput(nameResolver.resolveTvShow(video));

        assertTrue(video.getOutput().getFileName().toString().equals("Criminal Minds"));
    }
}
