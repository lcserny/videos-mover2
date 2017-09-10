package service;

import net.cserny.videosMover2.configuration.ServiceConfig;
import net.cserny.videosMover2.service.SubtitlesFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class SubtitleFindingTest extends TempVideoInitializer
{
    @Autowired
    private SubtitlesFinder subtitlesFinder;

    private List<Path> processSubtitles(String pathString) throws IOException {
        Path videoPath = Paths.get(pathString);
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
