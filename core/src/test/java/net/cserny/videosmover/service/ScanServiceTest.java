package net.cserny.videosmover.service;

import net.cserny.videosmover.ApplicationConfig;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.model.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
public class ScanServiceTest extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private ServiceFacade serviceFacade;

    @Test
    public void scan_whenEmptyFolderReturnEmptyList() throws Exception {
        serviceFacade.assertScannedVideos("/empty", true, null);
    }

    @Test
    public void scan_whenDownloadsReturnVideosList() throws Exception {
        serviceFacade.assertScannedVideos(StaticPathsProvider.getDownloadsPath(), false, null);
    }

    @Test
    public void scan_inputWithSubtitleRetainsSubtitle() throws Exception {
        serviceFacade.assertScannedVideos(DOWNLOADS_BIGSICK, false, DOWNLOADS_MOVIE_WITH_SUBTITLE);
    }

    @Test
    public void scan_scannedVideosShouldBeSortedByInput() throws Exception {
        List<Video> videos = serviceFacade.assertScannedVideos(StaticPathsProvider.getDownloadsPath(), false, null);
        List<Video> sortedVideos = new ArrayList<>(videos);
        sortedVideos.sort(Comparator.comparing(video -> video.getInput().getFileName().toString().toLowerCase()));
        for (int i = 0; i < videos.size(); i++) {
            assertEquals(sortedVideos.get(i), videos.get(i));
        }
    }
}
