package service;

import net.cserny.videosMover2.configuration.ServiceConfig;
import net.cserny.videosMover2.dto.AbstractSimpleFile;
import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.service.ScanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by leonardo on 02.09.2017.
 */
// TODO: use a custom File for Video input, output and subtitles list like SimpleFile and generate some before tests
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class ScanningTest
{
    @Autowired
    private ScanService scanService;

    private List<Video> videosScanned;

    public ScanningTest() throws IOException {
        videosScanned = scanService.scan(TestVideosProvider.getSingleMovieDirectoryDirectoryPath());
    }

    @Test
    public void whenScanningReturnVideosFromLocation() throws Exception {
        assertNotNull(videosScanned);
        assertFalse(videosScanned.isEmpty());
    }

    @Test
    public void givenVideoInputWithSubtitlesWhenScanningShouldReturnVideoWithSubtitles() throws Exception {
        for (Video video : videosScanned) {
            if (video.getInput().toString().contains(TestVideosProvider.getSingleMovieDirectoryMovieFile())) {
                List<AbstractSimpleFile> subtitles = video.getSubtitles();
                assertNotNull(subtitles);
                assertFalse(subtitles.isEmpty());
            }
        }
    }

    @Test
    public void scannedVideosShouldBeSortedByInput() throws Exception {
        List<Video> sortedVideos = new ArrayList<>(videosScanned);
        sortedVideos.sort(Comparator.comparing(video -> video.getInput().getPath().getFileName().toString().toLowerCase()));

        for (int i = 0; i < videosScanned.size(); i++) {
            assertEquals(sortedVideos.get(i), videosScanned.get(i));
        }
    }
}
