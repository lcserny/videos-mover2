package service;

import net.cserny.videosMover2.service.VideoChecker;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
public class VideoParsingTest
{
    @Autowired
    private VideoChecker videoChecker;

    private boolean isVideoResult(String pathString) throws IOException {
        return videoChecker.isVideo(Paths.get(pathString));
    }

    @Test
    public void givenDirectoryWhenParsingShouldReturnFalse() throws Exception {
        assertFalse(isVideoResult("/mnt"));
    }

    @Test
    public void givenRegularNonVideoFileWhenParsingShouldReturnFalse() throws Exception {
        assertFalse(isVideoResult("/etc/hosts"));
    }

    @Test
    public void givenVideoFileWhenParsingShouldReturnTrue() throws Exception {
        assertTrue(isVideoResult("/mnt/Data/Downloads/Nunta Leo si Sabina - Ciuleandra (Road Band).mp4"));
    }

    @Test
    public void givenSmallVideoFileWhenParsingShouldReturnFalse() throws Exception {
        assertFalse(isVideoResult("/mnt/Data/Downloads/Serj Tankian - Sky Is Over (OFFICIAL VIDEO).mp4"));
    }

    @Test
    public void givenVideoFileFromDisallowedPathWhenParsingShouldReturnFalse() throws Exception {
        assertFalse(isVideoResult("/mnt/Data/Downloads/Programming Stuff/criminal.minds.s12e11.720p.hdtv.hevc.x265.rmteam.mkv"));
    }
}
