package net.cserny.videosmover.service;

import net.cserny.videosmover.DaggerTestCoreComponent;
import net.cserny.videosmover.TestCoreComponent;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.helper.VideoCreator;
import net.cserny.videosmover.model.Video;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VideoCleanerTest extends InMemoryVideoFileSystemInitializer {

    @Inject
    OutputResolver outputResolver;

    @Inject
    VideoMover videoMover;

    @Inject
    VideoCleaner videoCleaner;

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
    public void clean_removesSourceParentFolder() throws Exception {
        Video video = VideoCreator.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE, outputResolver);
        assertCleaning(video, true);
    }

    @Test
    public void clean_whenDownloadsRootThenDoNotClean() throws Exception {
        Video video = VideoCreator.createMovie(DOWNLOADS_ROOT_VIDEO, outputResolver);
        assertCleaning(video, false);
    }

    @Test
    public void clean_whenRestrictedPathThenDoNotClean() throws Exception {
        Video video = VideoCreator.createMovie(DOWNLOADS_RESTRICTED_MOVIE, outputResolver);
        assertCleaning(video, false);
    }

    private void assertCleaning(Video video, boolean removeExpected) throws IOException {
        boolean moveSuccessful = videoMover.move(video);
        videoCleaner.clean(video);

        assertTrue(moveSuccessful);
        if (removeExpected) {
            assertFalse(Files.exists(video.getInputPath()));
        } else {
            assertTrue(Files.exists(video.getInputPath()));
        }
    }
}
