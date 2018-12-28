package net.cserny.videosmover.service;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import net.cserny.videosmover.CoreModule;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.model.VideoType;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class VideoMetadataServiceTest {

    @Inject
    CachedMetadataService metadataService;

    public VideoMetadataServiceTest() {
        Injector injector = Guice.createInjector(new CoreModule());
        injector.injectMembers(this);
    }

    @Test
    public void searchMovieMetadata_withEmptyQueryReturnEmptyList() throws Exception {
        Assume.assumeTrue(metadataService.isEnabled());

        List<VideoMetadata> videoMetadataList = metadataService.searchMetadata(
                VideoQuery.newInstance().withName("").build(), VideoType.MOVIE);
        assertTrue(videoMetadataList.isEmpty());
    }

    @Test
    public void searchMovieMetadata_byName() throws Exception {
        Assume.assumeTrue(metadataService.isEnabled());

        List<VideoMetadata> videoMetadataList = metadataService.searchMetadata(
                VideoQuery.newInstance().withName("Fight Club").withYear(1999).withLanguage("en").build(), VideoType.MOVIE);

        assertFalse(videoMetadataList.isEmpty());
        VideoMetadata metadata = videoMetadataList.get(0);

        assertEquals("Fight Club", metadata.getName());
        assertTrue(metadata.getReleaseDate().contains("1999"));
        assertFalse(metadata.getPosterUrl().isEmpty());
        assertFalse(metadata.getDescription().isEmpty());
        assertFalse(metadata.getCast().isEmpty());
    }

    @Test
    public void searchTvShowMetadata_byName() throws Exception {
        Assume.assumeTrue(metadataService.isEnabled());

        List<VideoMetadata> videoMetadataList = metadataService.searchMetadata(
                VideoQuery.newInstance().withName("Game of Thrones").build(), VideoType.TVSHOW);

        assertFalse(videoMetadataList.isEmpty());
        VideoMetadata metadata = videoMetadataList.get(0);

        assertFalse(metadata.getDescription().isEmpty());
        assertFalse(metadata.getPosterUrl().isEmpty());
        assertEquals("Game of Thrones", metadata.getName());
        assertFalse(metadata.getCast().isEmpty());
    }

    @Test
    public void searchTMDBInfoTest() throws Exception {
        Assume.assumeTrue(metadataService.isEnabled());

        Video video = new Video(null, null);
        video.setVideoType(VideoType.MOVIE);
        video.setOutputFolderWithoutDate("Fences");
        video.setYear(2016);

        metadataService.adjustOutputAndDate(video);

        assertEquals(video.getOutputFolderWithoutDate(), "Fences");
        assertEquals(video.getYear().toString(), "2016");
        assertEquals(video.getMonth().toString(), "12");
        assertEquals(video.getDay().toString(), "16");
    }
}
