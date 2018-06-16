package net.cserny.videosmover.service;

import net.cserny.videosmover.CoreTestComponent;
import net.cserny.videosmover.DaggerCoreTestComponent;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

public class VideoMetadataServiceTest {

    @Inject
    CachedTmdbService metadataService;

    @Before
    public void setUp() throws Exception {
        CoreTestComponent component = DaggerCoreTestComponent.create();
        component.inject(this);
    }

    @Test
    public void searchMovieMetadata_withEmptyQueryReturnEmptyList() throws Exception {
        List<VideoMetadata> videoMetadataList = metadataService.searchMovieMetadata(
                VideoQuery.newInstance().withName("").build());
        Assert.assertTrue(videoMetadataList.isEmpty());
    }

    @Test
    public void searchMovieMetadata_byName() throws Exception {
        List<VideoMetadata> videoMetadataList = metadataService.searchMovieMetadata(
                VideoQuery.newInstance().withName("Fight Club").withYear(1999).withLanguage("en").build());

        Assert.assertFalse(videoMetadataList.isEmpty());
        VideoMetadata metadata = videoMetadataList.get(0);

        Assert.assertEquals("Fight Club", metadata.getName());
        Assert.assertTrue(metadata.getReleaseDate().contains("1999"));
        Assert.assertFalse(metadata.getPosterUrl().isEmpty());
        Assert.assertFalse(metadata.getDescription().isEmpty());
        Assert.assertFalse(metadata.getCast().isEmpty());
    }

    @Test
    public void searchTvShowMetadata_byName() throws Exception {
        List<VideoMetadata> videoMetadataList = metadataService.searchTvShowMetadata(
                VideoQuery.newInstance().withName("Game of Thrones").build());

        Assert.assertFalse(videoMetadataList.isEmpty());
        VideoMetadata metadata = videoMetadataList.get(0);

        Assert.assertFalse(metadata.getDescription().isEmpty());
        Assert.assertFalse(metadata.getPosterUrl().isEmpty());
        Assert.assertEquals("Game of Thrones", metadata.getName());
        Assert.assertFalse(metadata.getCast().isEmpty());
    }
}
