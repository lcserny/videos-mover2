package service;

import net.cserny.videosMover2.MainApplication;
import net.cserny.videosMover2.service.PathsProvider;
import net.cserny.videosMover2.service.VideoChecker;
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
@ContextConfiguration(classes = MainApplication.class)
public class TestVideoValidation extends TmpVideoInitializer
{
    @Autowired
    private VideoChecker videoChecker;

    private boolean isVideoResult(String pathString) throws IOException {
        return videoChecker.isVideo(PathsProvider.getPath(pathString));
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
