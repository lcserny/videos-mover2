package net.cserny.videosmover.service;

import net.cserny.videosmover.CoreConfiguration;
import net.cserny.videosmover.helper.InMemoryFileSystem;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Subtitle;
import net.cserny.videosmover.model.Video;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static net.cserny.videosmover.helper.StaticPathsProvider.joinPaths;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CoreConfiguration.class)
public class ScanServiceTest {

    @Autowired
    ScanService scanService;

    private InMemoryFileSystem inMemoryFileSystem;

    @Before
    public void setUp() throws Exception {
        inMemoryFileSystem = new InMemoryFileSystem();
    }

    @After
    public void tearDown() throws Exception {
        inMemoryFileSystem.closeFileSystem();
    }

    @Test
    public void scan_whenEmptyFolderReturnEmptyList() throws Exception {
        List<Video> videosScanned = scanService.scan(StaticPathsProvider.getEmptyPath());
        assertNotNull(videosScanned);
        assertTrue(videosScanned.isEmpty());
    }

    @Test
    public void scan_whenDownloadsReturnVideosList() throws Exception {
        String bigSickPath = StaticPathsProvider.getDownloadsPath();
        String bigSickFile = "the.big.sick.2017.1080p.BluRay.x264.YIFY.mp4";

        inMemoryFileSystem.create(bigSickPath, null, bigSickFile, 2);

        List<Video> videosScanned = scanService.scan(StaticPathsProvider.getDownloadsPath());
        assertNotNull(videosScanned);
        assertFalse(videosScanned.isEmpty());
    }

    @Test
    public void scan_inputWithSubtitleRetainsSubtitle() throws Exception {
        String bigSickPath = StaticPathsProvider.getDownloadsPath();
        String bigSickFolder = "The.Big.Sick.1080p.[2017].x264";
        String bigSickFile = "the.big.sick.2017.1080p.BluRay.x264.YIFY.mp4";
        String bigSickSubFile = "Subtitle.srt";

        inMemoryFileSystem.create(bigSickPath, bigSickFolder, bigSickFile, 2);
        inMemoryFileSystem.create(bigSickPath, joinPaths(bigSickFolder, "Sub"), bigSickSubFile, 0);

        for (Video video : scanService.scan(joinPaths(bigSickPath, bigSickFolder))) {
            if (video.getFullInputPath().contains(joinPaths(bigSickPath, bigSickFolder))) {
                List<Subtitle> subtitles = video.getSubtitles();
                assertNotNull(subtitles);
                assertFalse(subtitles.isEmpty());
                assertEquals(1, subtitles.size());
            }
        }
    }

    @Test
    public void scan_scannedVideosShouldBeSortedByInput() throws Exception {
        String video1Path = StaticPathsProvider.getDownloadsPath();
        String video1Folder = "A-Video1";
        String video1File = "video1.mp4";

        String video2Path = StaticPathsProvider.getDownloadsPath();
        String video2Folder = "G-Video2";
        String video2File = "video2.mkv";

        String video3Path = StaticPathsProvider.getDownloadsPath();
        String video3Folder = "Z-Video3";
        String video3File = "video3.mp4";

        inMemoryFileSystem.create(video1Path, video1Folder, video1File, 2);
        inMemoryFileSystem.create(video2Path, video2Folder, video2File, 2);
        inMemoryFileSystem.create(video3Path, video3Folder, video3File, 2);

        List<Video> videosScanned = scanService.scan(StaticPathsProvider.getDownloadsPath());
        List<Video> sortedVideos = new ArrayList<>(videosScanned);
        sortedVideos.sort(Comparator.comparing(video -> video.getFullInputPath().toLowerCase()));
        for (int i = 0; i < videosScanned.size(); i++) {
            assertEquals(sortedVideos.get(i), videosScanned.get(i));
        }
    }
}
