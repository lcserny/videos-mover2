package service;

import net.cserny.videosMover2.configuration.ServiceConfig;
import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.dto.VideoRow;
import net.cserny.videosMover2.service.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Files;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class TestVideoCleanup extends TmpVideoInitializer
{
    @Autowired
    private OutputNameResolver nameResolver;
    @Autowired
    private VideoMover videoMover;
    @Autowired
    private VideoCleaner videoCleaner;
    @Autowired
    private RemovalRestrictionService removalRestrictionService;

    @Test
    public void cleaningVideoMeansRemovingSourceParentFolder() throws Exception {
        Video video = new Video();
        video.setInput(PathsProvider.getPath(DOWNLOADS_MOVIE_WITH_SUBTITLE));

        videoCleaner.clean(video);

        assertTrue(!Files.exists(video.getInput().getParent()));
    }

    @Test
    public void whenSourceParentFolderIsDownloadsThenDoNotRemoveIt() throws Exception {
        Video video = new Video();
        video.setInput(PathsProvider.getPath(DOWNLOADS_ROOT_VIDEO));

        videoCleaner.clean(video);

        assertTrue(Files.exists(video.getInput().getParent()));
    }

    @Test
    public void afterMovingVideoSourceFolderShouldBeClean() throws Exception {
        Video video = new Video();
        video.setInput(PathsProvider.getPath(DOWNLOADS_MOVIE_WITH_SUBTITLE));

        VideoRow videoRow = new VideoRow(video);
        videoRow.setIsMovie(true);
        videoRow.setOutput(nameResolver.resolve(video));

        assertTrue(videoMover.move(video));
        videoCleaner.clean(video);

        assertTrue(!Files.exists(video.getInput().getParent()));
    }

    @Test
    public void whenCleaningVideoFromRestrictedRemovalPathThenDontRemoveIt() throws Exception {
        Video video = new Video();
        video.setInput(PathsProvider.getPath(DOWNLOADS_MOVIE_WITH_SUBTITLE));
        String parentFolderName = video.getInput().getParent().getFileName().toString();
        removalRestrictionService.addRestriction(parentFolderName);

        videoCleaner.clean(video);

        removalRestrictionService.getRestrictedFolders().remove(parentFolderName);
        Assert.assertTrue(Files.exists(video.getInput().getParent()));
    }
}
