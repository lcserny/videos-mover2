package net.cserny.videosmover.service;

import net.cserny.videosmover.ApplicationConfig;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
public class SubtitleFinderTest extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private ServiceFacade serviceFacade;

    @Test
    public void find_withoutSubtitlesReturnsEmptyList() throws Exception {
        serviceFacade.assertSubtitlesFound(DOWNLOADS_TVSHOW, true);
    }

    @Test
    public void find_withSubtitlesReturnsSubtitlesList() throws Exception {
        serviceFacade.assertSubtitlesFound(DOWNLOADS_MOVIE_WITH_SUBTITLE, false);
    }

    @Test
    public void find_fromDownloadsRootReturnsEmptyList() throws Exception {
        serviceFacade.assertSubtitlesFound(DOWNLOADS_ROOT_VIDEO, true);
    }
}
