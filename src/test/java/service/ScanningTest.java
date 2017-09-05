package service;

import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.service.ScanService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by leonardo on 02.09.2017.
 */
public class ScanningTest
{
    private List<Video> videosScanned;

    @Autowired
    public ScanningTest(ScanService scanService) throws IOException {
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

    @Test
    public void scannedVideosShouldBeSortedByInput() throws Exception {
        List<Video> sortedVideos = new ArrayList<>(videosScanned);
        sortedVideos.sort(Comparator.comparing(video -> video.getInput().getFileName().toString().toLowerCase()));

        for (int i = 0; i < videosScanned.size(); i++) {
            assertEquals(sortedVideos.get(i), videosScanned.get(i));
        }
    }
}
