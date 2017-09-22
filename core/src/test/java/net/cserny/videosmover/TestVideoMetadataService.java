package net.cserny.videosmover;

import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.service.VideoMetadataService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class TestVideoMetadataService {
    @Autowired
    private VideoMetadataService metadataService;

    @Test
    public void testSearchMovieByEmptyQuery() throws Exception {
        List<VideoMetadata> videoMetadataList = metadataService.searchMovieMetadata(
                VideoQuery.newInstance().withName("").build());
        Assert.assertTrue(videoMetadataList.isEmpty());
    }

    @Test
    public void testSearchMovieByName() throws Exception {
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
    public void testSearchTvShowByName() throws Exception {
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
