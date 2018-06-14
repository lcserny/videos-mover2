package net.cserny.videosmover.service;

import net.cserny.videosmover.DaggerTestCoreComponent;
import net.cserny.videosmover.TestCoreComponent;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.helper.VideoCreator;
import net.cserny.videosmover.model.Video;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

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
        assertThat(video.getOutputFolderName(), containsString("1922 (2017)"));
    }

    @Test
    public void resolve_tvShowInputReturnsCorrectOutput() throws Exception {
        Video video = VideoCreator.createTvShow(DOWNLOADS_TVSHOW, outputResolver);
        assertTrue(video.getOutputPath().startsWith(StaticPathsProvider.getTvShowsPath()));
        assertEquals("Game Of Thrones", video.getOutputFolderName());
    }

    @Test
    public void resolve_movieInputReturnsCorrectOutput() throws Exception {
        Video video = VideoCreator.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE, outputResolver);
        assertTrue(video.getOutputPath().startsWith(StaticPathsProvider.getMoviesPath()));
        assertEquals("The Big Sick (2017)", video.getOutputFolderName());

        Video video2 = VideoCreator.createMovie(DOWNLOADS_MOVIE_ACRIMONY, outputResolver);
        assertTrue(video2.getOutputPath().startsWith(StaticPathsProvider.getMoviesPath()));
        assertEquals("Acrimony (2018)", video2.getOutputFolderName());
    }

    @Test
    public void resolve_tvShowInputSetsOutputToExistingTvShowName() throws Exception {
        Video video = VideoCreator.createTvShow(DOWNLOADS_EXISTING_TVSHOW, outputResolver);
        assertTrue(video.getOutputPath().startsWith(StaticPathsProvider.getTvShowsPath()));
        assertEquals("Criminal Minds", video.getOutputFolderName());

        Video video2 = VideoCreator.createTvShow(DOWNLOADS_TVSHOW2, outputResolver);
        assertTrue(video2.getOutputPath().startsWith(StaticPathsProvider.getTvShowsPath()));
        assertEquals("Chicago PD", video2.getOutputFolderName());

        Video video3 = VideoCreator.createTvShow(DOWNLOADS_TVSHOW3, outputResolver);
        assertTrue(video3.getOutputPath().startsWith(StaticPathsProvider.getTvShowsPath()));
        assertEquals("Chicago Med", video3.getOutputFolderName());
    }
}