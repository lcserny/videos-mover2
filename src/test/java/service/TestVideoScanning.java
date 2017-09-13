package service;

import net.cserny.videosMover2.MainApplication;
import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.service.PathsProvider;
import net.cserny.videosMover2.service.ScanService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MainApplication.class)
public class TestVideoScanning extends TmpVideoInitializer
{
    @Autowired
    private ScanService scanService;

    private List<Video> videosScanned;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        videosScanned = scanService.scan(PathsProvider.getDownloadsPath());
    }

    @Test
    public void whenScanningReturnVideosFromLocation() throws Exception {
        assertNotNull(videosScanned);
        assertFalse(videosScanned.isEmpty());
    }

    @Test
    public void givenVideoInputWithSubtitlesWhenScanningShouldReturnVideoWithSubtitles() throws Exception {
        for (Video video : videosScanned) {
            if (video.getInput().toString().contains(DOWNLOADS_MOVIE_WITH_SUBTITLE)) {
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
