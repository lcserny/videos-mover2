package service;

import net.cserny.videosMover2.configuration.ServiceConfig;
import net.cserny.videosMover2.service.VideoChecker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
// TODO: use a custom File for Video input, output and subtitles list like SimpleFile and generate some before tests
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class VideoParsingTest
{
    @Autowired
    private VideoChecker videoChecker;

    private boolean isVideoResult(String pathString) throws IOException {
        return videoChecker.isVideo(Paths.get(pathString));
    }

    @Test
    public void givenDirectoryWhenParsingShouldReturnFalse() throws Exception {
        assertFalse(isVideoResult(TestVideosProvider.getDirectoryPath()));
    }

    @Test
    public void givenRegularNonVideoFileWhenParsingShouldReturnFalse() throws Exception {
        assertFalse(isVideoResult(TestVideosProvider.getNonVideoFilePath()));
    }

    @Test
    public void givenVideoFileWhenParsingShouldReturnTrue() throws Exception {
        assertTrue(isVideoResult(TestVideosProvider.getTvShowFilePath()));
    }

    @Test
    public void givenSmallVideoFileWhenParsingShouldReturnFalse() throws Exception {
        assertFalse(isVideoResult(TestVideosProvider.getSmallMovieFilePath()));
    }

    @Test
    public void givenVideoFileFromDisallowedPathWhenParsingShouldReturnFalse() throws Exception {
        assertFalse(isVideoResult(TestVideosProvider.getVideoFromDisallowedFilePath()));
    }
}
