package net.cserny.videosmover.service;

import net.cserny.videosmover.DaggerTestCoreComponent;
import net.cserny.videosmover.TestCoreComponent;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.helper.VideoCreator;
import net.cserny.videosmover.model.Video;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;

public class OutputResolverTest extends InMemoryVideoFileSystemInitializer {

    @Inject
    OutputResolver outputResolver;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        TestCoreComponent component = DaggerTestCoreComponent.create();
        component.inject(this);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void resolve_allDigitsMovieResolvesNameCorrectly() throws Exception {
        Video video = VideoCreator.createMovie(DOWNLOADS_MOVIE_ALL_DIGITS, outputResolver);
        assertThat(video.getOutputFilename(), containsString("1922 (2017)"));
    }

    @Test
    public void resolve_tvShowInputReturnsCorrectOutput() throws Exception {
        Video video = VideoCreator.createTvShow(DOWNLOADS_TVSHOW, outputResolver);
        assertTrue(video.getOutputPath().startsWith(StaticPathsProvider.getTvShowsPath()));
        assertEquals("Game Of Thrones", video.getOutputFilename());
    }

    @Test
    public void resolve_movieInputReturnsCorrectOutput() throws Exception {
        Video video = VideoCreator.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE, outputResolver);
        assertTrue(video.getOutputPath().startsWith(StaticPathsProvider.getMoviesPath()));
        assertEquals("The Big Sick (2017)", video.getOutputFilename());
    }

    @Test
    public void resolve_tvShowInputSetsOutputToExistingTvShowName() throws Exception {
        Video video = VideoCreator.createTvShow(DOWNLOADS_EXISTING_TVSHOW, outputResolver);
        assertEquals("Criminal Minds", video.getOutputFilename());
    }
}