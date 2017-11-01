package net.cserny.videosmover.service;

import net.cserny.videosmover.ApplicationConfig;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.model.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class OutputResolverSpec extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private OutputResolver outputResolver;

    @Test
    public void allDigitsMovieShouldResolvNameCorrectly() throws Exception {
        Video video = new Video();
        video.setInput(StaticPathsProvider.getPath(DOWNLOADS_MOVIE_ALL_DIGITS));
        video.setIsMovie(true);
        assertThat(outputResolver.resolve(video), containsString("1922 (2017)"));
    }
}