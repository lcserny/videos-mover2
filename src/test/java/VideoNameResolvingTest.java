import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.service.VideoOutputNameResolver;
import net.cserny.videosMover2.service.VideoOutputNameResolverImpl;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * Created by leonardo on 02.09.2017.
 */
public class VideoNameResolvingTest
{
    private VideoOutputNameResolver nameResolver = new VideoOutputNameResolverImpl();

//    @Test
//    public void givenTvShowVideoInputWhenParsingShouldReturnTvShowOutput() throws Exception {
//
//    }

//    @Test
//    public void givenTvShowVideoInputWhenParsingReturnsTrimmedOutput() throws Exception {
//        // "criminal.minds.s12e11.720p.hdtv.hevc.x265.rmteam.mkv"
//        // "criminal.minds"
//
//        Video video = new Video();
//        video.setIsTvShow(true);
//        video.setInput(Paths.get("/mnt/Data/Downloads/www.torrenting.com - Criminal.Minds.S12E05.HDTV.x264-FLEET/Criminal.Minds.S12E05.HDTV.x264-FLEET.mkv"));
//        video.setOutput();
//    }

//    @Test
//    public void givenMovieVideoInputWhenParsingReturnsTrimmedOutput() throws Exception {
//        Video video = new Video();
//        video.setIsMovie(true);
//        video.setInput(Paths.get("/mnt/Data/Downloads/Nunta Leo si Sabina - Ciuleandra (Road Band).mp4"));
//    }
}
