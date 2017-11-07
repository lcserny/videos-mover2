package net.cserny.videosmover.service;

import net.cserny.videosmover.ApplicationConfig;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.helper.VideoCreator;
import net.cserny.videosmover.model.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationConfig.class})
public class VideoCleanerTest extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private OutputResolver outputResolver;
    @Autowired
    private VideoMover videoMover;
    @Autowired
    private VideoCleaner videoCleaner;

    @Test
    public void cleaningVideoMeansRemovingSourceParentFolder() throws Exception {
        Video video = VideoCreator.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE, outputResolver);
        assertCleaning(video, true);
    }

    @Test
    public void whenSourceParentFolderIsDownloadsThenDoNotRemoveIt() throws Exception {
        Video video = VideoCreator.createMovie(DOWNLOADS_ROOT_VIDEO, outputResolver);
        assertCleaning(video, false);
    }

    @Test
    public void whenCleaningVideoFromRestrictedRemovalPathThenDontRemoveIt() throws Exception {
        Video video = VideoCreator.createMovie(DOWNLOADS_RESTRICTED_MOVIE, outputResolver);
        assertCleaning(video, false);
    }

    private void assertCleaning(Video video, boolean removeExpected) throws IOException {
        boolean moveSuccessful = videoMover.move(video);
        videoCleaner.clean(video);

        assertTrue(moveSuccessful);
        if (removeExpected) {
            assertFalse(Files.exists(video.getInput().getParent()));
        } else {
            assertTrue(Files.exists(video.getInput().getParent()));
        }
    }
}
