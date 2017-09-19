package net.cserny.videosmover;

import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.helper.TestHelperConfig;
import net.cserny.videosmover.helper.VideoCreationHelper;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.VideoCleaner;
import net.cserny.videosmover.service.VideoMover;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class, TestHelperConfig.class})
public class TestVideoCleanup extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private VideoCreationHelper videoHelper;
    @Autowired
    private VideoMover videoMover;
    @Autowired
    private VideoCleaner videoCleaner;

    @Test
    public void cleaningVideoMeansRemovingSourceParentFolder() throws Exception {
        Video video = videoHelper.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE);
        assertCleaning(video, true);
    }

    @Test
    public void whenSourceParentFolderIsDownloadsThenDoNotRemoveIt() throws Exception {
        Video video = videoHelper.createMovie(DOWNLOADS_ROOT_VIDEO);
        assertCleaning(video, false);
    }

    @Test
    public void afterMovingVideoSourceFolderShouldBeClean() throws Exception {
        Video video = videoHelper.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE);
        assertCleaning(video, true);
    }

    @Test
    public void whenCleaningVideoFromRestrictedRemovalPathThenDontRemoveIt() throws Exception {
        Video video = videoHelper.createMovie(DOWNLOADS_RESTRICTED_MOVIE);
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
