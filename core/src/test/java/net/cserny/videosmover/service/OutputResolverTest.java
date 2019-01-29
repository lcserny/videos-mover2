package net.cserny.videosmover.service;

import net.cserny.videosmover.CoreConfiguration;
import net.cserny.videosmover.helper.InMemoryFileSystem;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.helper.VideoResolver;
import net.cserny.videosmover.model.Video;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static net.cserny.videosmover.helper.StaticPathsProvider.joinPaths;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreConfiguration.class)
public class OutputResolverTest {

    @Autowired
    OutputResolver outputResolver;

    private InMemoryFileSystem inMemoryFileSystem;

    @Before
    public void setUp() throws Exception {
        inMemoryFileSystem = InMemoryFileSystem.initFileSystem();
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
        Video video = VideoResolver.resolveMovie(moviePath,
                joinPaths(moviePath, movieFolder, movieFile),
                outputResolver::resolve);

        assertThat(video.getOutputFolderWithoutDate(), containsString("1922"));
    }

    @Test
    public void resolve_tvShowInputReturnsCorrectOutput() throws Exception {
        String tvPath = StaticPathsProvider.getDownloadsPath();
        String tvFolder = "Game.Of.Thrones.s0e10";
        String tvFile = "game.of.thrones.s07e06.720p.A.Song.Of.Ice.And.Fire.x264.mp4";

        inMemoryFileSystem.create(tvPath, tvFolder, tvFile, 2);
        Video video = VideoResolver.resolveTvShow(tvPath,
                joinPaths(tvPath, tvFolder, tvFile),
                outputResolver::resolve);

        assertEquals("Game Of Thrones", video.getOutputFolderWithoutDate());
    }

    @Test
    public void resolve_movieInputReturnsCorrectOutput() throws Exception {
        String bigSickPath = StaticPathsProvider.getDownloadsPath();
        String bigSickFolder = "The.Big.Sick.1080p.[2017].x264";
        String bigSickFile = "the.big.sick.2017.1080p.BluRay.x264.YIFY.mp4";

        inMemoryFileSystem.create(bigSickPath, bigSickFolder, bigSickFile, 2);
        Video bigSick = VideoResolver.resolveMovie(bigSickPath,
                joinPaths(bigSickPath, bigSickFolder, bigSickFile),
                outputResolver::resolve);

        assertEquals("The Big Sick", bigSick.getOutputFolderWithoutDate());

        String acrimonyPath = StaticPathsProvider.getDownloadsPath();
        String acrimonyFolder = "Acrimony.2018.1080p.WEB-DL.DD5.1.H264-FGT";
        String acrimonyFile = "Acrimony.2018.1080p.WEB-DL.DD5.1.H264-FGT.mkv";

        inMemoryFileSystem.create(acrimonyPath, acrimonyFolder, acrimonyFile, 2);
        Video acrimony = VideoResolver.resolveMovie(acrimonyPath,
                joinPaths(acrimonyPath, acrimonyFolder, acrimonyFile),
                outputResolver::resolve);

        assertEquals("Acrimony (2018)", acrimony.getOutputFolderWithDate());
    }

    @Test
    public void resolve_tvShowInputSetsOutputToExistingTvShowName() throws Exception {
        String criminalMindsPath = StaticPathsProvider.getDownloadsPath();
        String criminalMindsFolder = "Criminal.Minds.s01e01";
        String criminalMindsFile = "criminil.mids.s01e01.720p.x264.mp4";

        inMemoryFileSystem.create(criminalMindsPath, criminalMindsFolder, criminalMindsFile, 2);
        Video criminalMinds = VideoResolver.resolveTvShow(criminalMindsPath,
                joinPaths(criminalMindsPath, criminalMindsFolder, criminalMindsFile),
                outputResolver::resolve);

        assertEquals("Criminal Minds", criminalMinds.getOutputFolderWithoutDate());

        String chicagoPdPath = StaticPathsProvider.getDownloadsPath();
        String chicagoPdFolder = "Chicago.PD.S05E21.REPACK.HDTV.x264-KILLERS[rarbg]";
        String chicagoPdFile = "Chicago.PD.S05E21.REPACK.HDTV.x264-KILLERS.mkv";

        inMemoryFileSystem.create(chicagoPdPath, chicagoPdFolder, chicagoPdFile, 2);
        Video chicagoPd = VideoResolver.resolveTvShow(chicagoPdPath,
                joinPaths(chicagoPdPath, chicagoPdFolder, chicagoPdFile),
                outputResolver::resolve);

        assertEquals("Chicago PD", chicagoPd.getOutputFolderWithoutDate());

        String chicagoMedPath = StaticPathsProvider.getDownloadsPath();
        String chicagoMedFile = "Chicago.Med.S03E14.HDTV.x264-KILLERS[eztv].mkv";

        inMemoryFileSystem.create(chicagoMedPath, null, chicagoMedFile, 2);
        Video chicagoMed = VideoResolver.resolveTvShow(chicagoMedPath,
                joinPaths(chicagoMedPath, chicagoMedFile),
                outputResolver::resolve);

        assertEquals("Chicago Med", chicagoMed.getOutputFolderWithoutDate());
    }

    @Test
    public void resolve_tvShowWithSeasonInName() throws Exception {
        String houseOfCardsPath = StaticPathsProvider.getDownloadsPath();
        String houseOfCardsFolder = "House.of.Cards.S06.1080p.NF.WEBRip.DD5.1.x264-NTG[rartv]";
        String houseOfCardsFile = "House.of.Cards.US.S06E01.Chapter.66.1080p.NF.WEB-DL.DD5.1.x264-NTG.mkv";

        inMemoryFileSystem.create(houseOfCardsPath, houseOfCardsFolder, houseOfCardsFile, 2);
        Video houseOfCards = VideoResolver.resolveTvShow(houseOfCardsPath,
                joinPaths(houseOfCardsPath, houseOfCardsFolder, houseOfCardsFile),
                outputResolver::resolve);

        assertEquals("House Of Cards", houseOfCards.getOutputFolderWithoutDate());

        String houseOfCardsPath2 = StaticPathsProvider.getDownloadsPath();
        String houseOfCardsFolder2 = "House of Cards s06";
        String houseOfCardsFile2 = "House.of.Cards.US.S06E01.Chapter.66.1080p.NF.WEB-DL.DD5.1.x264-NTG.mkv";

        inMemoryFileSystem.create(houseOfCardsPath2, houseOfCardsFolder2, houseOfCardsFile2, 2);
        Video houseOfCards2 = VideoResolver.resolveTvShow(houseOfCardsPath2,
                joinPaths(houseOfCardsPath2, houseOfCardsFolder2, houseOfCardsFile2),
                outputResolver::resolve);

        assertEquals("House Of Cards", houseOfCards2.getOutputFolderWithoutDate());
    }

    @Test
    public void resolve_doubleYearInTVShow() throws Exception {
        String existingExtrasPath = StaticPathsProvider.getTvShowsPath();
        String existingExtrasFolder = "Extras (2005)";
        inMemoryFileSystem.create(existingExtrasPath, existingExtrasFolder, null, 0);

        String extrasPath = StaticPathsProvider.getDownloadsPath();
        String extrasFolder = "Extras S02e01-06";
        String extrasFile = "Extras S02.Ciak.Sbagliati.Ppb1.avi";
        inMemoryFileSystem.create(extrasPath, extrasFolder, extrasFile, 2);

        Video extras = VideoResolver.resolveTvShow(extrasPath,
                joinPaths(extrasPath, extrasFolder, extrasFile),
                outputResolver::resolve);

        assertEquals("Extras (2005)", extras.getOutputFolderWithDate());
    }
}