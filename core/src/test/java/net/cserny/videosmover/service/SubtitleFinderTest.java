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
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SubtitleFinderTest {

    @Inject
    SubtitlesFinder subtitlesFinder;

    private InMemoryFileSystem inMemoryFileSystem;

    public SubtitleFinderTest() {
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

    private List<Path> processSubtitles(String pathString) throws IOException {
        Path videoPath = StaticPathsProvider.getPath(pathString);
        return subtitlesFinder.find(videoPath);
    }

    @Test
    public void find_withoutSubtitlesReturnsEmptyList() throws Exception {
        String tvPath = StaticPathsProvider.getDownloadsPath();
        String tvFolder = "Game.Of.Thrones.s0e10";
        String tvFile = "game.of.thrones.s07e06.720p.A.Song.Of.Ice.And.Fire.x264.mp4";

        inMemoryFileSystem.create(tvPath, tvFolder, tvFile, 2);

        List<Path> subtitles = processSubtitles(StaticPathsProvider.getJoinedPathString(false, tvPath, tvFolder, tvFile));
        assertTrue(subtitles.isEmpty());
    }

    @Test
    public void find_withSubtitlesReturnsSubtitlesList() throws Exception {
        String bigSickPath = StaticPathsProvider.getDownloadsPath();
        String bigSickFolder = "The.Big.Sick.1080p.[2017].x264";
        String bigSickFile = "the.big.sick.2017.1080p.BluRay.x264.YIFY.mp4";
        String bigSickSubFile = "subtitle.srt";

        inMemoryFileSystem.create(bigSickPath, bigSickFolder, bigSickFile, 2);
        inMemoryFileSystem.create(bigSickPath, bigSickFolder +
                StaticPathsProvider.getPathString(true, "Sub"), bigSickSubFile, 0);

        List<Path> subtitles = processSubtitles(StaticPathsProvider.getJoinedPathString(false, bigSickPath, bigSickFolder, bigSickFile));
        assertFalse(subtitles.isEmpty());
    }

    @Test
    public void find_fromDownloadsRootReturnsEmptyList() throws Exception {
        String videoPath = StaticPathsProvider.getDownloadsPath();
        String videoFile = "fromDownloads.mp4";

        inMemoryFileSystem.create(videoPath, null, videoFile, 2);

        List<Path> subtitles = processSubtitles(StaticPathsProvider.getJoinedPathString(false, videoPath, videoFile));
        assertTrue(subtitles.isEmpty());
    }
}
