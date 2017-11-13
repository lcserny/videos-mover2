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
public class VideoCheckerTest extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private ServiceFacade serviceFacade;

    @Test
    public void isVideo_parsingDirectoryReturnsFalse() throws Exception {
        serviceFacade.assertVideo(DOWNLOADS_EMPTY_FOLDER, false);
    }

    @Test
    public void isVideo_parsingNonVideoFileReturnsFalse() throws Exception {
        serviceFacade.assertVideo(DOWNLOADS_REGULAR_FILE, false);
    }

    @Test
    public void isVideo_parsingVideoFileReturnsTrue() throws Exception {
        serviceFacade.assertVideo(DOWNLOADS_TVSHOW, true);
    }

    @Test
    public void isVideo_parsingSmallVideoFileReturnsFalse() throws Exception {
        serviceFacade.assertVideo(DOWNLOADS_SMALL_VIDEO, false);
    }

    @Test
    public void isVideo_parsingVideoFileFromDisallowedPathReturnsFalse() throws Exception {
        serviceFacade.assertVideo(DOWNLOADS_ILLEGAL_VIDEO, false);
    }
}
