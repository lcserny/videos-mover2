package net.cserny.videosmover.service;

import net.cserny.videosmover.DaggerTestCoreComponent;
import net.cserny.videosmover.TestCoreComponent;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
@Ignore
public class ScanServiceTest extends InMemoryVideoFileSystemInitializer {

    @Inject
    ScanService scanService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        TestCoreComponent component = DaggerTestCoreComponent.create();
        component.inject(this);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void scan_whenEmptyFolderReturnEmptyList() throws Exception {
        List<Video> videosScanned = scanService.scan("/empty");
        assertNotNull(videosScanned);
        assertTrue(videosScanned.isEmpty());
    }

    @Test
    public void scan_whenDownloadsReturnVideosList() throws Exception {
        List<Video> videosScanned = scanService.scan(StaticPathsProvider.getDownloadsPath());
        assertNotNull(videosScanned);
        assertFalse(videosScanned.isEmpty());
    }

    @Test
    public void scan_inputWithSubtitleRetainsSubtitle() throws Exception {
        List<Video> videoWithSubtitle = scanService.scan(DOWNLOADS_BIGSICK);
        for (Video video : videoWithSubtitle) {
            if (video.getInputPath().toString().contains(DOWNLOADS_MOVIE_WITH_SUBTITLE)) {
                List<Path> subtitles = video.getSubtitles();
                assertNotNull(subtitles);
                assertFalse(subtitles.isEmpty());
                assertEquals(1, subtitles.size());
            }
        }
    }

    @Test
    public void scan_scannedVideosShouldBeSortedByInput() throws Exception {
        List<Video> videosScanned = scanService.scan(StaticPathsProvider.getDownloadsPath());
        List<Video> sortedVideos = new ArrayList<>(videosScanned);
        sortedVideos.sort(Comparator.comparing(video -> video.getInputFilename().toLowerCase()));
        for (int i = 0; i < videosScanned.size(); i++) {
            assertEquals(sortedVideos.get(i), videosScanned.get(i));
        }
    }
}
