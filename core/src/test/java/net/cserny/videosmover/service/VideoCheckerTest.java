package net.cserny.videosmover.service;

import net.cserny.videosmover.DaggerTestCoreComponent;
import net.cserny.videosmover.TestCoreComponent;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.helper.StaticPathsProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VideoCheckerTest extends InMemoryVideoFileSystemInitializer {

    @Inject
    VideoChecker videoChecker;

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

    private boolean isVideoResult(String pathString) throws IOException {
        return videoChecker.isVideo(StaticPathsProvider.getPath(pathString));
    }

    @Test
    public void isVideo_parsingDirectoryReturnsFalse() throws Exception {
        assertFalse(isVideoResult(DOWNLOADS_EMPTY_FOLDER));
    }

    @Test
    public void isVideo_parsingNonVideoFileReturnsFalse() throws Exception {
        assertFalse(isVideoResult(DOWNLOADS_REGULAR_FILE));
    }

    @Test
    public void isVideo_parsingVideoFileReturnsTrue() throws Exception {
        assertTrue(isVideoResult(DOWNLOADS_TVSHOW));
    }

    @Test
    public void isVideo_parsingSmallVideoFileReturnsFalse() throws Exception {
        assertFalse(isVideoResult(DOWNLOADS_SMALL_VIDEO));
    }

    @Test
    public void isVideo_parsingVideoFileFromDisallowedPathReturnsFalse() throws Exception {
        assertFalse(isVideoResult(DOWNLOADS_ILLEGAL_VIDEO));
    }
}
