package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.helper.InMemoryFileSystem;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Collections;

import static net.cserny.videosmover.helper.StaticPathsProvider.joinPaths;
import static org.junit.Assert.assertEquals;

public class VideoNameTrimmerTest {

    private VideoNameTrimmer videoNameTrimmer = new VideoNameTrimmer();
    private InMemoryFileSystem inMemoryFileSystem;

    @Before
    public void setUp() throws Exception {
        inMemoryFileSystem = InMemoryFileSystem.initFileSystem();
    }

    @After
    public void tearDown() throws Exception {
        inMemoryFileSystem.closeFileSystem();
    }

    @Test
    public void extensionWithLetterAndDigitIsTrimmedCorrectly() {
        String pathString = joinPaths(StaticPathsProvider.getDownloadsPath(), "leo.mp4");
        Path path = StaticPathsProvider.getPath(pathString);
        String fileName = path.getFileName().toString();
        Video video = new Video(fileName, path.toString());
        video.setVideoType(VideoType.MOVIE);
        video.setOutputFolderWithoutDateFromFilename();

        videoNameTrimmer.parseMovie(video, Collections.emptyList());

        assertEquals("Leo", video.getOutputFolderWithoutDate());
    }
}