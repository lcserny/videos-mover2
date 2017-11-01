package net.cserny.videosmover;

import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.service.StaticPathsProvider;
import net.cserny.videosmover.service.VideoChecker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class TestVideoValidation extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private VideoChecker videoChecker;

    private boolean isVideoResult(String pathString) throws IOException {
        return videoChecker.isVideo(StaticPathsProvider.getPath(pathString));
    }

    @Test
    public void givenDirectoryWhenParsingShouldReturnFalse() throws Exception {
        assertFalse(isVideoResult(DOWNLOADS_EMPTY_FOLDER));
    }

    @Test
    public void givenRegularNonVideoFileWhenParsingShouldReturnFalse() throws Exception {
        assertFalse(isVideoResult(DOWNLOADS_REGULAR_FILE));
    }

    @Test
    public void givenVideoFileWhenParsingShouldReturnTrue() throws Exception {
        assertTrue(isVideoResult(DOWNLOADS_TVSHOW));
    }

    @Test
    public void givenSmallVideoFileWhenParsingShouldReturnFalse() throws Exception {
        assertFalse(isVideoResult(DOWNLOADS_SMALL_VIDEO));
    }

    @Test
    public void givenVideoFileFromDisallowedPathWhenParsingShouldReturnFalse() throws Exception {
        assertFalse(isVideoResult(DOWNLOADS_ILLEGAL_VIDEO));
    }
}
