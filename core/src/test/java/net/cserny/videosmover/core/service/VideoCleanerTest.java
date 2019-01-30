package net.cserny.videosmover.core.service;

import net.cserny.videosmover.core.CoreConfiguration;
import net.cserny.videosmover.core.helper.InMemoryFileSystem;
import net.cserny.videosmover.core.helper.StaticPathsProvider;
import net.cserny.videosmover.core.helper.VideoResolver;
import net.cserny.videosmover.core.model.Video;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CoreConfiguration.class)
public class VideoCleanerTest {

    @Autowired
    OutputResolver outputResolver;

    @Autowired
    VideoMover videoMover;

    @Autowired
    VideoCleaner videoCleaner;

    private InMemoryFileSystem inMemoryFileSystem;

    @Before
    public void setUp() throws Exception {
        inMemoryFileSystem = new InMemoryFileSystem();
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
        Video video = VideoResolver.resolveMovie(moviePath,
                StaticPathsProvider.joinPaths(moviePath, movieFolder, movieFile),
                outputResolver::resolve);

        assertCleaning(video, true);
    }

    @Test
    public void clean_whenDownloadsRootThenDoNotClean() throws Exception {
        String videoPath = StaticPathsProvider.getDownloadsPath();
        String videoFile = "fromDownloads.mp4";

        inMemoryFileSystem.create(videoPath, null, videoFile, 2);
        Video video = VideoResolver.resolveMovie(videoPath,
                StaticPathsProvider.joinPaths(videoPath, videoFile),
                outputResolver::resolve);

        assertCleaning(video, false);
    }

    @Test
    public void clean_whenRestrictedPathThenDoNotClean() throws Exception {
        String videoPath = StaticPathsProvider.getDownloadsPath();
        String videoFolder = "The.Hero.1080p.[2017].x264";
        String videoFile = "the.hero.2017.1080p.BluRay.x264.YIFY.mp4";

        inMemoryFileSystem.create(videoPath, videoFolder, videoFile, 2);
        Video video = VideoResolver.resolveMovie(videoPath,
                StaticPathsProvider.joinPaths(videoPath, videoFolder, videoFile),
                outputResolver::resolve);

        assertCleaning(video, false);
    }

    private void assertCleaning(Video video, boolean removeExpected) throws IOException {
        boolean moveSuccessful = videoMover.move(video);
        Path inputPath = StaticPathsProvider.getPath(video.getInputPathWithoutFileName());
        videoCleaner.clean(inputPath);

        assertTrue(moveSuccessful);
        if (removeExpected) {
            assertFalse(Files.exists(inputPath));
        } else {
            assertTrue(Files.exists(inputPath));
        }
    }
}
