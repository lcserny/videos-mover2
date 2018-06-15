package net.cserny.videosmover.service;

import net.cserny.videosmover.CoreTestComponent;
import net.cserny.videosmover.DaggerCoreTestComponent;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.helper.StaticPathsProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
@Ignore
public class SubtitleFinderTest extends InMemoryVideoFileSystemInitializer {

    @Inject
    SubtitlesFinder subtitlesFinder;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        CoreTestComponent component = DaggerCoreTestComponent.create();
        component.inject(this);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    private List<Path> processSubtitles(String pathString) throws IOException {
        Path videoPath = StaticPathsProvider.getPath(pathString);
        return subtitlesFinder.find(videoPath);
    }

    @Test
    public void find_withoutSubtitlesReturnsEmptyList() throws Exception {
        List<Path> subtitles = processSubtitles(DOWNLOADS_TVSHOW);
        assertTrue(subtitles.isEmpty());
    }

    @Test
    public void find_withSubtitlesReturnsSubtitlesList() throws Exception {
        List<Path> subtitles = processSubtitles(DOWNLOADS_MOVIE_WITH_SUBTITLE);
        assertFalse(subtitles.isEmpty());
    }

    @Test
    public void find_fromDownloadsRootReturnsEmptyList() throws Exception {
        List<Path> subtitles = processSubtitles(DOWNLOADS_ROOT_VIDEO);
        assertTrue(subtitles.isEmpty());
    }
}
