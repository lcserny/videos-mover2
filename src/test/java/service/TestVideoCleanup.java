package service;

import net.cserny.videosMover2.MainApplication;
import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.dto.VideoRow;
import net.cserny.videosMover2.service.OutputNameResolver;
import net.cserny.videosMover2.service.PathsProvider;
import net.cserny.videosMover2.service.VideoCleaner;
import net.cserny.videosMover2.service.VideoMover;
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
    private OutputNameResolver nameResolver;

    @Autowired
    private VideoMover videoMover;

    @Autowired
    private VideoCleaner videoCleaner;

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
}
