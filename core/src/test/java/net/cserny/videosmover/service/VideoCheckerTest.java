package net.cserny.videosmover.service;

import net.cserny.videosmover.CoreTestComponent;
import net.cserny.videosmover.DaggerCoreTestComponent;
import net.cserny.videosmover.helper.InMemoryFileSystem;
import net.cserny.videosmover.helper.StaticPathsProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VideoCheckerTest {

    @Inject
    VideoChecker videoChecker;

    private InMemoryFileSystem inMemoryFileSystem;

    public VideoCheckerTest() {
        CoreTestComponent component = DaggerCoreTestComponent.create();
        component.inject(this);
    }

    @Before
    public void setUp() throws Exception {
        inMemoryFileSystem = InMemoryFileSystem.initFileSystem();
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

        assertFalse(isVideoResult(StaticPathsProvider.getJoinedPathString(false, videoPath, videoFolder)));
    }

    @Test
    public void isVideo_parsingNonVideoFileReturnsFalse() throws Exception {
        String videoPath = StaticPathsProvider.getDownloadsPath();
        String videoFolder = "NonVideoFolder";
        String videoFile = "NonVideo.txt";

        inMemoryFileSystem.create(videoPath, videoFolder, videoFile, 0);

        assertFalse(isVideoResult(StaticPathsProvider.getJoinedPathString(false, videoPath, videoFolder, videoFile)));
    }

    @Test
    public void isVideo_parsingVideoFileReturnsTrue() throws Exception {
        String videoPath = StaticPathsProvider.getDownloadsPath();
        String videoFolder = "Game.Of.Thrones.s0e10";
        String videoFile = "game.of.thrones.s07e06.720p.A.Song.Of.Ice.And.Fire.x264.mp4";

        inMemoryFileSystem.create(videoPath, videoFolder, videoFile, 2);

        assertTrue(isVideoResult(StaticPathsProvider.getJoinedPathString(false, videoPath, videoFolder, videoFile)));
    }

    @Test
    public void isVideo_parsingSmallVideoFileReturnsFalse() throws Exception {
        String videoPath = StaticPathsProvider.getDownloadsPath();
        String videoFolder = "SmallVideoFolder";
        String videoFile = "smallVideo.mp4";

        inMemoryFileSystem.create(videoPath, videoFolder, videoFile, 1);

        assertFalse(isVideoResult(StaticPathsProvider.getJoinedPathString(false, videoPath, videoFolder, videoFile)));
    }

    @Test
    public void isVideo_parsingVideoFileFromDisallowedPathReturnsFalse() throws Exception {
        String videoPath = StaticPathsProvider.getDownloadsPath();
        String videoFolder = "Programming Stuff";
        String videoFile = "illegalVideo.mp4";

        inMemoryFileSystem.create(videoPath, videoFolder, videoFile, 2);

        assertFalse(isVideoResult(StaticPathsProvider.getJoinedPathString(false, videoPath, videoFolder, videoFile)));
    }
}
