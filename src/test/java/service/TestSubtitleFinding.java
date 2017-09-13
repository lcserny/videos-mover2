package service;

import net.cserny.videosMover2.MainApplication;
import net.cserny.videosMover2.service.PathsProvider;
import net.cserny.videosMover2.service.SubtitlesFinder;
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
@ContextConfiguration(classes = MainApplication.class)
public class TestSubtitleFinding extends TmpVideoInitializer
{
    @Autowired
    private SubtitlesFinder subtitlesFinder;

    private List<Path> processSubtitles(String pathString) throws IOException {
        Path videoPath = PathsProvider.getPath(pathString);
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
