package net.cserny.videosmover.service;

import net.cserny.videosmover.CoreConfiguration;
import net.cserny.videosmover.helper.InMemoryFileSystem;
import net.cserny.videosmover.helper.StaticPathsProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CoreConfiguration.class)
public class VideoCheckerTest {

    @Autowired
    VideoChecker videoChecker;

    private InMemoryFileSystem inMemoryFileSystem;

    @Before
    public void setUp() throws Exception {
        inMemoryFileSystem = new InMemoryFileSystem();
    }

    @After
    public void tearDown() throws Exception {
        inMemoryFileSystem.closeFileSystem();
    }

    private boolean isVideoResult(String pathString) throws IOException {
        return videoChecker.isVideo(StaticPathsProvider.getPath(pathString));
    }

    @Test
    public void isVideo_parsingDirectoryReturnsFalse() throws Exception {
        String videoPath = StaticPathsProvider.getDownloadsPath();
        String videoFolder = "emptyFolder";

        inMemoryFileSystem.create(videoPath, videoFolder, null, 0);

        assertFalse(isVideoResult(StaticPathsProvider.joinPaths(videoPath, videoFolder)));
    }

    @Test
    public void isVideo_parsingNonVideoFileReturnsFalse() throws Exception {
        String videoPath = StaticPathsProvider.getDownloadsPath();
        String videoFolder = "NonVideoFolder";
        String videoFile = "NonVideo.txt";

        inMemoryFileSystem.create(videoPath, videoFolder, videoFile, 0);

        assertFalse(isVideoResult(StaticPathsProvider.joinPaths(videoPath, videoFolder, videoFile)));
    }

    @Test
    public void isVideo_parsingVideoFileReturnsTrue() throws Exception {
        String videoPath = StaticPathsProvider.getDownloadsPath();
        String videoFolder = "Game.Of.Thrones.s0e10";
        String videoFile = "game.of.thrones.s07e06.720p.A.Song.Of.Ice.And.Fire.x264.mp4";

        inMemoryFileSystem.create(videoPath, videoFolder, videoFile, 2);

        assertTrue(isVideoResult(StaticPathsProvider.joinPaths(videoPath, videoFolder, videoFile)));
    }

    @Test
    public void isVideo_parsingSmallVideoFileReturnsFalse() throws Exception {
        String videoPath = StaticPathsProvider.getDownloadsPath();
        String videoFolder = "SmallVideoFolder";
        String videoFile = "smallVideo.mp4";

        inMemoryFileSystem.create(videoPath, videoFolder, videoFile, 1);

        assertFalse(isVideoResult(StaticPathsProvider.joinPaths(videoPath, videoFolder, videoFile)));
    }

    @Test
    public void isVideo_parsingVideoFileFromDisallowedPathReturnsFalse() throws Exception {
        String videoPath = StaticPathsProvider.getDownloadsPath();
        String videoFolder = "Programming Stuff";
        String videoFile = "illegalVideo.mp4";

        inMemoryFileSystem.create(videoPath, videoFolder, videoFile, 2);

        assertFalse(isVideoResult(StaticPathsProvider.joinPaths(videoPath, videoFolder, videoFile)));
    }
}
