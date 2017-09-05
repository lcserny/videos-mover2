package service;

import com.google.inject.Inject;
import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.service.SubtitlesFinder;
import net.cserny.videosMover2.service.SubtitlesFinderImpl;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
public class SubtitleFindingTest
{
    @Inject
    private SubtitlesFinder subtitlesFinder;

    private List<Path> processSubtitles(String pathString) throws IOException {
        Path videoPath = Paths.get(pathString);
        return subtitlesFinder.find(videoPath);
    }

    @Test
    public void givenVideoWithoutSubtitlesWhenFindingReturnsEmptyList() throws Exception {
        List<Path> subtitles = processSubtitles("/mnt/Data/Downloads/[ www.torrenting.com ] - Criminal.Minds.S12E01.HDTV.x264-FLEET/Criminal.Minds.S12E01.HDTV.x264-FLEET.mkv");
        assertTrue(subtitles.isEmpty());
    }

    @Test
    public void givenVideoWithSubtitlesWhenFindingReturnsSubtitlesList() throws Exception {
        List<Path> subtitles = processSubtitles("/mnt/Data/Downloads/71 (2014) [1080p]/71.2014.1080p.BluRay.x264.YIFY.mkv");
        assertFalse(subtitles.isEmpty());
    }

    @Test
    public void givenVideoFromDownloadsRootPathWhenFindingReturnsEmptySubtitlesList() throws Exception {
        List<Path> subtitles = processSubtitles("/mnt/Data/Downloads/Nunta Leo si Sabina - Ciuleandra (Road Band).mp4");
        assertTrue(subtitles.isEmpty());
    }
}
