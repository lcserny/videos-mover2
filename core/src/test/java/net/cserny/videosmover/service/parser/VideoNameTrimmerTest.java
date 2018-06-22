package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.VideoPath;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class VideoNameTrimmerTest {

    private VideoNameTrimmer videoNameTrimmer = new VideoNameTrimmer();

    @Test
    public void extensionWithLetterAndDigitIsTrimmedCorrectly() {
        VideoPath videoPath = new VideoPath(StaticPathsProvider.getDownloadsPath(), "leo.mp4", null);
        videoNameTrimmer.parseMovie(videoPath, Collections.emptyList());
        assertEquals("Leo", videoPath.getOutputFolder());
    }
}