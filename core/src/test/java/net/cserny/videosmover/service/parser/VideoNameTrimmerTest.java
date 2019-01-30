package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.CoreConfiguration;
import net.cserny.videosmover.helper.InMemoryFileSystem;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Path;
import java.util.Collections;

import static net.cserny.videosmover.helper.StaticPathsProvider.joinPaths;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CoreConfiguration.class)
public class VideoNameTrimmerTest {

    @Autowired
    VideoNameTrimmer videoNameTrimmer;

    private InMemoryFileSystem inMemoryFileSystem;

    @Before
    public void setUp() throws Exception {
        inMemoryFileSystem = new InMemoryFileSystem();
    }

    @After
    public void tearDown() throws Exception {
        inMemoryFileSystem.closeFileSystem();
    }

    @Test
    public void trim_extensionWithLetterAndDigitIsTrimmedCorrectly() {
        String pathString = joinPaths(StaticPathsProvider.getDownloadsPath(), "leo.mp4");
        Path path = StaticPathsProvider.getPath(pathString);
        String fileName = path.getFileName().toString();
        Video video = new Video(fileName, path.toString());
        video.setVideoType(VideoType.MOVIE);
        video.setOutputFolderWithoutDateFromFilename();

        videoNameTrimmer.parseMovie(video, Collections.emptyList());

        assertEquals("Leo", video.getOutputFolderWithoutDate());
    }

    @Test
    public void trim_seriesNumberOnlyInName() throws Exception {
        String folder = "Bodyguard-S01-Series.1--BBC-2018-720p-w.subs-x265-HEVC";
        String fileName = "Bodyguard-S01E01.mp4";
        String pathString = joinPaths(StaticPathsProvider.getDownloadsPath(), folder, fileName);
        Video video = new Video(fileName, StaticPathsProvider.getPath(pathString).toString());
        video.setVideoType(VideoType.TVSHOW);
        video.setOutputFolderWithoutDate(folder);

        videoNameTrimmer.parseTvShow(video, Collections.emptyList());

        assertEquals("Bodyguard", video.getOutputFolderWithoutDate());
    }
}