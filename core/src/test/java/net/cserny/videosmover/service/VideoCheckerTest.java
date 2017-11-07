package net.cserny.videosmover.service;

import net.cserny.videosmover.ApplicationConfig;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
public class VideoCheckerTest extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private VideoChecker videoChecker;

    private boolean isVideoResult(String pathString) throws IOException {
        return videoChecker.isVideo(StaticPathsProvider.getPath(pathString));
    }

    @Test
    public void parsingDirectoryReturnsFalse() throws Exception {
        assertFalse(isVideoResult(DOWNLOADS_EMPTY_FOLDER));
    }

    @Test
    public void parsingNonVideoFileReturnsFalse() throws Exception {
        assertFalse(isVideoResult(DOWNLOADS_REGULAR_FILE));
    }

    @Test
    public void parsingVideoFileReturnsTrue() throws Exception {
        assertTrue(isVideoResult(DOWNLOADS_TVSHOW));
    }

    @Test
    public void parsingSmallVideoFileReturnsFalse() throws Exception {
        assertFalse(isVideoResult(DOWNLOADS_SMALL_VIDEO));
    }

    @Test
    public void parsingVideoFileFromDisallowedPathReturnsFalse() throws Exception {
        assertFalse(isVideoResult(DOWNLOADS_ILLEGAL_VIDEO));
    }
}
