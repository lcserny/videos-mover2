package net.cserny.videosmover.service;

import net.cserny.videosmover.ApplicationConfig;
import net.cserny.videosmover.model.VideoQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
public class VideoMetadataServiceTest {
    @Autowired
    private ServiceFacade serviceFacade;

    @Test
    public void searchMovieMetadata_withEmptyQueryReturnEmptyList() throws Exception {
        serviceFacade.assertMovieMetadata(VideoQuery.newInstance().withName("").build(), null, null);
    }

    @Test
    public void searchMovieMetadata_byName() throws Exception {
        VideoQuery movieQuery = VideoQuery.newInstance().withName("Fight Club").withYear(1999).withLanguage("en").build();
        serviceFacade.assertMovieMetadata(movieQuery, "Fight Club", "1999");
    }

    @Test
    public void searchTvShowMetadata_byName() throws Exception {
        VideoQuery tvShowQuery = VideoQuery.newInstance().withName("Game of Thrones").build();
        serviceFacade.assertTvShowMetadata(tvShowQuery, "Game of Thrones");
    }
}
