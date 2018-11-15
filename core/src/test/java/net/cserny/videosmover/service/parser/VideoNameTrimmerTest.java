package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoType;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Collections;

import static net.cserny.videosmover.helper.StaticPathsProvider.getJoinedPathString;
import static org.junit.Assert.assertEquals;

public class VideoNameTrimmerTest {

    private VideoNameTrimmer videoNameTrimmer = new VideoNameTrimmer();

    @Test
    public void extensionWithLetterAndDigitIsTrimmedCorrectly() {
        String pathString = getJoinedPathString(StaticPathsProvider.getDownloadsPath(), "leo.mp4");
        Path path = StaticPathsProvider.getPath(pathString);
        String fileName = path.getFileName().toString();
        Video video = new Video(fileName, path.toString());
        video.setVideoType(VideoType.MOVIE);
        video.setOutputFolderWithoutDate(fileName);

        videoNameTrimmer.parseMovie(video, Collections.emptyList());

        assertEquals("Leo", video.getOutputFolderWithoutDate());
    }
}