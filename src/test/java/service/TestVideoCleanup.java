package service;

import net.cserny.videosMover2.MainApplication;
import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.dto.VideoRow;
import net.cserny.videosMover2.service.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MainApplication.class)
public class TestVideoCleanup extends TmpVideoInitializer
{
    @Autowired
    private VideoMover videoMover;
    @Autowired
    private VideoCleaner videoCleaner;

    @Test
    public void cleaningVideoMeansRemovingSourceParentFolder() throws Exception {
        Video video = createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE);

        videoCleaner.clean(video);

        assertTrue(!Files.exists(video.getInput().getParent()));
    }

    @Test
    public void whenSourceParentFolderIsDownloadsThenDoNotRemoveIt() throws Exception {
        Video video = createMovie(DOWNLOADS_ROOT_VIDEO);

        videoCleaner.clean(video);

        assertTrue(Files.exists(video.getInput().getParent()));
    }

    @Test
    public void afterMovingVideoSourceFolderShouldBeClean() throws Exception {
        Video video = createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE);

        boolean moveSuccessful = videoMover.move(video);
        videoCleaner.clean(video);

        assertTrue(moveSuccessful);
        assertTrue(!Files.exists(video.getInput().getParent()));
    }

    @Test
    public void whenCleaningVideoFromRestrictedRemovalPathThenDontRemoveIt() throws Exception {
        Video video = createMovie(DOWNLOADS_RESTRICTED_MOVIE);

        videoCleaner.clean(video);

        Assert.assertTrue(Files.exists(video.getInput().getParent()));
    }
}
