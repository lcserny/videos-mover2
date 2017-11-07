package net.cserny.videosmover.service;

import net.cserny.videosmover.ApplicationConfig;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.model.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest(classes = ApplicationConfig.class)
public class ScanServiceSpec extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private ScanService scanService;

    @Test
    public void whenEmptyFolderReturnEmptyList() throws Exception {
        List<Video> videosScanned = scanService.scan("/empty");
        assertNotNull(videosScanned);
        assertTrue(videosScanned.isEmpty());
    }

    @Test
    public void whenDownloadsReturnVideosList() throws Exception {
        List<Video> videosScanned = scanService.scan(StaticPathsProvider.getDownloadsPath());
        assertNotNull(videosScanned);
        assertFalse(videosScanned.isEmpty());
    }

    @Test
    public void inputWithSubtitleRetainsTheSubtitle() throws Exception {
        List<Video> videoWithSubtitle = scanService.scan(DOWNLOADS_BIGSICK);
        for (Video video : videoWithSubtitle) {
            if (video.getInput().toString().contains(DOWNLOADS_MOVIE_WITH_SUBTITLE)) {
                List<Path> subtitles = video.getSubtitles();
                assertNotNull(subtitles);
                assertFalse(subtitles.isEmpty());
                assertEquals(1, subtitles.size());
            }
        }
    }

    @Test
    public void scannedVideosShouldBeSortedByInput() throws Exception {
        List<Video> videosScanned = scanService.scan(StaticPathsProvider.getDownloadsPath());
        List<Video> sortedVideos = new ArrayList<>(videosScanned);
        sortedVideos.sort(Comparator.comparing(video -> video.getInput().getFileName().toString().toLowerCase()));
        for (int i = 0; i < videosScanned.size(); i++) {
            assertEquals(sortedVideos.get(i), videosScanned.get(i));
        }
    }
}
