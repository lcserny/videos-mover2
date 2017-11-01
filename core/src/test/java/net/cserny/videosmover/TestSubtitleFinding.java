package net.cserny.videosmover;

import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.service.StaticPathsProvider;
import net.cserny.videosmover.service.SubtitlesFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class TestSubtitleFinding extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private SubtitlesFinder subtitlesFinder;

    private List<Path> processSubtitles(String pathString) throws IOException {
        Path videoPath = StaticPathsProvider.getPath(pathString);
        return subtitlesFinder.find(videoPath);
    }

    @Test
    public void givenVideoWithoutSubtitlesWhenFindingReturnsEmptyList() throws Exception {
        List<Path> subtitles = processSubtitles(DOWNLOADS_TVSHOW);
        assertTrue(subtitles.isEmpty());
    }

    @Test
    public void givenVideoWithSubtitlesWhenFindingReturnsSubtitlesList() throws Exception {
        List<Path> subtitles = processSubtitles(DOWNLOADS_MOVIE_WITH_SUBTITLE);
        assertFalse(subtitles.isEmpty());
    }

    @Test
    public void givenVideoFromDownloadsRootPathWhenFindingReturnsEmptySubtitlesList() throws Exception {
        List<Path> subtitles = processSubtitles(DOWNLOADS_ROOT_VIDEO);
        assertTrue(subtitles.isEmpty());
    }
}
