package net.cserny.videosmover.service;

import net.cserny.videosmover.CoreTestComponent;
import net.cserny.videosmover.DaggerCoreTestComponent;
import net.cserny.videosmover.helper.InMemoryFileSystem;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.helper.VideoResolver;
import net.cserny.videosmover.model.Video;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VideoCleanerTest {

    @Inject
    OutputResolver outputResolver;

    @Inject
    VideoMover videoMover;

    @Inject
    VideoCleaner videoCleaner;

    private InMemoryFileSystem inMemoryFileSystem;

    public VideoCleanerTest() {
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

    @Test
    public void clean_removesSourceParentFolder() throws Exception {
        String moviePath = StaticPathsProvider.getDownloadsPath();
        String movieFolder = "The.Big.Sick.1080p.[2017].x264";
        String movieFile = "the.big.sick.2017.1080p.BluRay.x264.YIFY.mp4";

        inMemoryFileSystem.create(moviePath, movieFolder, movieFile, 2);
        Video video = VideoResolver.resolveMovie(String.join("/", moviePath, movieFolder, movieFile),
                outputResolver::resolve);

        assertCleaning(video, true);
    }

    @Test
    public void clean_whenDownloadsRootThenDoNotClean() throws Exception {
        String videoPath = StaticPathsProvider.getDownloadsPath();
        String videoFile = "fromDownloads.mp4";

        inMemoryFileSystem.create(videoPath, null, videoFile, 2);
        Video video = VideoResolver.resolveMovie(String.join("/", videoPath, videoFile),
                outputResolver::resolve);

        assertCleaning(video, false);
    }

    @Test
    public void clean_whenRestrictedPathThenDoNotClean() throws Exception {
        String videoPath = StaticPathsProvider.getDownloadsPath();
        String videoFolder = "The.Hero.1080p.[2017].x264";
        String videoFile = "the.hero.2017.1080p.BluRay.x264.YIFY.mp4";

        inMemoryFileSystem.create(videoPath, videoFolder, videoFile, 2);
        Video video = VideoResolver.resolveMovie(String.join("/", videoPath, videoFolder, videoFile),
                outputResolver::resolve);

        assertCleaning(video, false);
    }

    private void assertCleaning(Video video, boolean removeExpected) throws IOException {
        boolean moveSuccessful = videoMover.move(video);
        Path inputPath = video.getInputPath();
        videoCleaner.clean(inputPath);

        assertTrue(moveSuccessful);
        if (removeExpected) {
            assertFalse(Files.exists(inputPath));
        } else {
            assertTrue(Files.exists(inputPath));
        }
    }
}
