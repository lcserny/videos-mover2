package net.cserny.videosmover.service;

import net.cserny.videosmover.CoreTestComponent;
import net.cserny.videosmover.DaggerCoreTestComponent;
import net.cserny.videosmover.helper.InMemoryFileSystem;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.helper.VideoResolver;
import net.cserny.videosmover.model.Video;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.inject.Inject;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;

public class OutputResolverTest {

    @Inject
    OutputResolver outputResolver;

    private InMemoryFileSystem inMemoryFileSystem;

    @Before
    public void setUp() throws Exception {
        inMemoryFileSystem = InMemoryFileSystem.initFileSystem();
        CoreTestComponent component = DaggerCoreTestComponent.create();
        component.inject(this);
    }

    @After
    public void tearDown() throws Exception {
        inMemoryFileSystem.closeFileSystem();
    }

    @Test
    public void resolve_allDigitsMovieResolvesNameCorrectly() throws Exception {
        String moviePath = StaticPathsProvider.getDownloadsPath();
        String movieFolder = "1922.1080p.[2017].x264";
        String movieFile = "1922.2017.1080p.BluRay.x264.YIFY.mp4";

        inMemoryFileSystem.create(moviePath, movieFolder, movieFile, 2);
        Video video = VideoResolver.resolveMovie(String.join("/", moviePath, movieFolder, movieFile),
                outputResolver::resolve);

        assertThat(video.getOutputFolderName(), containsString("1922"));
    }

    @Test
    public void resolve_tvShowInputReturnsCorrectOutput() throws Exception {
        String tvPath = StaticPathsProvider.getDownloadsPath();
        String tvFolder = "Game.Of.Thrones.s0e10";
        String tvFile = "game.of.thrones.s07e06.720p.A.Song.Of.Ice.And.Fire.x264.mp4";

        inMemoryFileSystem.create(tvPath, tvFolder, tvFile, 2);
        Video video = VideoResolver.resolveTvShow(String.join("/", tvPath, tvFolder, tvFile),
                outputResolver::resolve);

        assertTrue(video.getOutputPath().startsWith(StaticPathsProvider.getTvShowsPath()));
        assertEquals("Game Of Thrones", video.getOutputFolderName());
    }

    @Test
    public void resolve_movieInputReturnsCorrectOutput() throws Exception {
        String bigSickPath = StaticPathsProvider.getDownloadsPath();
        String bigSickFolder = "The.Big.Sick.1080p.[2017].x264";
        String bigSickFile = "the.big.sick.2017.1080p.BluRay.x264.YIFY.mp4";

        inMemoryFileSystem.create(bigSickPath, bigSickFolder, bigSickFile, 2);
        Video bigSick = VideoResolver.resolveMovie(String.join("/", bigSickPath, bigSickFolder, bigSickFile),
                outputResolver::resolve);

        assertTrue(bigSick.getOutputPath().startsWith(StaticPathsProvider.getMoviesPath()));
        assertEquals("The Big Sick", bigSick.getOutputFolderName());

        String acrimonyPath = StaticPathsProvider.getDownloadsPath();
        String acrimonyFolder = "Acrimony.2018.1080p.WEB-DL.DD5.1.H264-FGT";
        String acrimonyFile = "Acrimony.2018.1080p.WEB-DL.DD5.1.H264-FGT.mkv";

        inMemoryFileSystem.create(acrimonyPath, acrimonyFolder, acrimonyFile, 2);
        Video acrimony = VideoResolver.resolveMovie(String.join("/", acrimonyPath, acrimonyFolder, acrimonyFile),
                outputResolver::resolve);

        assertTrue(acrimony.getOutputPath().startsWith(StaticPathsProvider.getMoviesPath()));
        assertEquals("Acrimony (2018)", acrimony.getOutputFolderName());
    }

    @Test
    public void resolve_tvShowInputSetsOutputToExistingTvShowName() throws Exception {
        String criminalMindsPath = StaticPathsProvider.getDownloadsPath();
        String criminalMindsFolder = "Criminal.Minds.s01e01";
        String criminalMindsFile = "criminil.mids.s01e01.720p.x264.mp4";

        inMemoryFileSystem.create(criminalMindsPath, criminalMindsFolder, criminalMindsFile, 2);
        Video criminalMinds = VideoResolver.resolveTvShow(String.join("/", criminalMindsPath, criminalMindsFolder, criminalMindsFile),
                outputResolver::resolve);

        assertTrue(criminalMinds.getOutputPath().startsWith(StaticPathsProvider.getTvShowsPath()));
        assertEquals("Criminal Minds", criminalMinds.getOutputFolderName());

        String chicagoPdPath = StaticPathsProvider.getDownloadsPath();
        String chicagoPdFolder = "Chicago.PD.S05E21.REPACK.HDTV.x264-KILLERS[rarbg]";
        String chicagoPdFile = "Chicago.PD.S05E21.REPACK.HDTV.x264-KILLERS.mkv";

        inMemoryFileSystem.create(chicagoPdPath, chicagoPdFolder, chicagoPdFile, 2);
        Video chicagoPd = VideoResolver.resolveTvShow(String.join("/", chicagoPdPath, chicagoPdFolder, chicagoPdFile),
                outputResolver::resolve);

        assertTrue(chicagoPd.getOutputPath().startsWith(StaticPathsProvider.getTvShowsPath()));
        assertEquals("Chicago PD", chicagoPd.getOutputFolderName());

        String chicagoMedPath = StaticPathsProvider.getDownloadsPath();
        String chicagoMedFile = "Chicago.Med.S03E14.HDTV.x264-KILLERS[eztv].mkv";

        inMemoryFileSystem.create(chicagoMedPath, null, chicagoMedFile, 2);
        Video chicagoMed = VideoResolver.resolveTvShow(String.join("/", chicagoMedPath, chicagoMedFile),
                outputResolver::resolve);

        assertTrue(chicagoMed.getOutputPath().startsWith(StaticPathsProvider.getTvShowsPath()));
        assertEquals("Chicago Med", chicagoMed.getOutputFolderName());
    }
}