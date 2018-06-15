package net.cserny.videosmover.service;

import net.cserny.videosmover.CoreTestComponent;
import net.cserny.videosmover.CoreTestModule;
import net.cserny.videosmover.DaggerCoreTestComponent;
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

//    @Inject
//    CachedTmdbService cachedTmdbService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        CoreTestComponent component = DaggerCoreTestComponent.builder()
//                .coreModule(new CoreTestModule())
                .build();
        component.inject(this);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void resolve_allDigitsMovieResolvesNameCorrectly() throws Exception {
//        VideoMetadata movie1922Metadata = new VideoMetadata();
//        movie1922Metadata.setName("1922");
//        movie1922Metadata.setReleaseDate("2017-10-20");
//        VideoQuery movie1922Query = VideoQuery.newInstance().withName("1922").build();
//        when(cachedTmdbService.searchMovieMetadata(movie1922Query))
//                .thenReturn(Collections.singletonList(movie1922Metadata));

        Video video = VideoCreator.createMovie(DOWNLOADS_MOVIE_ALL_DIGITS, outputResolver);

        assertThat(video.getOutputFolderName(), containsString("1922"));
    }

    @Test
    public void resolve_tvShowInputReturnsCorrectOutput() throws Exception {
//        VideoMetadata gotMetadata = new VideoMetadata();
//        gotMetadata.setName("Game of Thrones");
//        gotMetadata.setReleaseDate("2011-04-17");
//        VideoQuery gotQuery = VideoQuery.newInstance().withName("Game Of Thrones").build();
//        when(cachedTmdbService.searchTvShowMetadata(gotQuery))
//                .thenReturn(Collections.singletonList(gotMetadata));

        Video video = VideoCreator.createTvShow(DOWNLOADS_TVSHOW, outputResolver);

        assertTrue(video.getOutputPath().startsWith(StaticPathsProvider.getTvShowsPath()));
        assertEquals("Game Of Thrones", video.getOutputFolderName());
    }

    @Test
    public void resolve_movieInputReturnsCorrectOutput() throws Exception {
//        VideoMetadata bigSickMetadata = new VideoMetadata();
//        bigSickMetadata.setName("The Big Sick");
//        bigSickMetadata.setReleaseDate("2017-06-23");
//        VideoQuery bigSickQuery = VideoQuery.newInstance().withName("The Big Sick").build();
//        when(cachedTmdbService.searchMovieMetadata(bigSickQuery))
//                .thenReturn(Collections.singletonList(bigSickMetadata));
//        VideoMetadata acrimonyMetadata = new VideoMetadata();
//        acrimonyMetadata.setName("Acrimony");
//        acrimonyMetadata.setReleaseDate("2018-03-30");
//        VideoQuery acrimonyQuery = VideoQuery.newInstance().withName("Acrimony").withYear(2018).build();
//        when(cachedTmdbService.searchMovieMetadata(acrimonyQuery))
//                .thenReturn(Collections.singletonList(acrimonyMetadata));

        Video video = VideoCreator.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE, outputResolver);
        Video video2 = VideoCreator.createMovie(DOWNLOADS_MOVIE_ACRIMONY, outputResolver);

        assertTrue(video.getOutputPath().startsWith(StaticPathsProvider.getMoviesPath()));
        assertEquals("The Big Sick", video.getOutputFolderName());
        assertTrue(video2.getOutputPath().startsWith(StaticPathsProvider.getMoviesPath()));
        assertEquals("Acrimony (2018)", video2.getOutputFolderName());
    }

    @Test
    public void resolve_tvShowInputSetsOutputToExistingTvShowName() throws Exception {
//        VideoMetadata criminalMindsMetadata = new VideoMetadata();
//        criminalMindsMetadata.setName("Criminal Minds");
//        criminalMindsMetadata.setReleaseDate("2005-09-22");
//        VideoQuery criminalMindsQuery = VideoQuery.newInstance().withName("Criminal Minds").build();
//        when(cachedTmdbService.searchTvShowMetadata(criminalMindsQuery))
//                .thenReturn(Collections.singletonList(criminalMindsMetadata));
//
//        VideoMetadata chicagoPDMetadata = new VideoMetadata();
//        chicagoPDMetadata.setName("Chicago P.D.");
//        chicagoPDMetadata.setReleaseDate("2014-01-08");
//        VideoQuery chicagoPDQuery = VideoQuery.newInstance().withName("Chicago PD").build();
//        when(cachedTmdbService.searchTvShowMetadata(chicagoPDQuery))
//                .thenReturn(Collections.singletonList(chicagoPDMetadata));
//
//        VideoMetadata chicagoMedMetadata = new VideoMetadata();
//        chicagoMedMetadata.setName("Chicago Med");
//        chicagoMedMetadata.setReleaseDate("2015-11-17");
//        VideoQuery chicagoMedQuery = VideoQuery.newInstance().withName("Chicago Med").build();
//        when(cachedTmdbService.searchTvShowMetadata(chicagoMedQuery))
//                .thenReturn(Collections.singletonList(chicagoMedMetadata));

        Video video = VideoCreator.createTvShow(DOWNLOADS_EXISTING_TVSHOW, outputResolver);
        Video video2 = VideoCreator.createTvShow(DOWNLOADS_TVSHOW2, outputResolver);
        Video video3 = VideoCreator.createTvShow(DOWNLOADS_TVSHOW3, outputResolver);

        assertTrue(video.getOutputPath().startsWith(StaticPathsProvider.getTvShowsPath()));
        assertEquals("Criminal Minds", video.getOutputFolderName());
        assertTrue(video2.getOutputPath().startsWith(StaticPathsProvider.getTvShowsPath()));
        assertEquals("Chicago PD", video2.getOutputFolderName());
        assertTrue(video3.getOutputPath().startsWith(StaticPathsProvider.getTvShowsPath()));
        // TODO: running from app, this still resolves into Downloads???
        assertEquals("Chicago Med", video3.getOutputFolderName());
    }
}