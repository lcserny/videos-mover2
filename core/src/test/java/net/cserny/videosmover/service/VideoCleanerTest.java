package net.cserny.videosmover.service;

import net.cserny.videosmover.ApplicationConfig;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.model.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationConfig.class})
public class VideoCleanerTest extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private OutputResolver outputResolver;
    @Autowired
    private ServiceFacade serviceFacade;

    @Test
    public void clean_removesSourceParentFolder() throws Exception {
        Video video = serviceFacade.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE);
        serviceFacade.assertCleaning(video, true);
    }

    @Test
    public void clean_whenDownloadsRootThenDoNotClean() throws Exception {
        Video video = serviceFacade.createMovie(DOWNLOADS_ROOT_VIDEO);
        serviceFacade.assertCleaning(video, false);
    }

    @Test
    public void clean_whenRestrictedPathThenDoNotClean() throws Exception {
        Video video = serviceFacade.createMovie(DOWNLOADS_RESTRICTED_MOVIE);
        serviceFacade.assertCleaning(video, false);
    }
}
