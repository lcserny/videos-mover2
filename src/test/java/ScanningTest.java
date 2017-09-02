import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.service.*;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by leonardo on 02.09.2017.
 */
public class ScanningTest
{
    private VideoOutputNameResolver videoOutputNameResolver = new VideoOutputNameResolverImpl();
    private List<Video> videosScanned;

    public ScanningTest() throws IOException {
        ScanService scanService = new ScanServiceImpl(new VideoCheckerImpl(), new SubtitlesFinderImpl());
        String location = "/mnt/Data/Downloads/";
        videosScanned = scanService.scan(location);
    }

    @Test
    public void whenScanningReturnVideosFromLocation() throws Exception {
        assertNotNull(videosScanned);
        assertFalse(videosScanned.isEmpty());
    }

    @Test
    public void givenVideoInputWithSubtitlesWhenScanningShouldReturnVideoWithSubtitles() throws Exception {
        for (Video video : videosScanned) {
            if (video.getInput().toString().contains("71.2014.1080p.BluRay.x264.YIFY.mkv")) {
                List<Path> subtitles = video.getSubtitles();
                assertNotNull(subtitles);
                assertFalse(subtitles.isEmpty());
            }
        }
    }
}
