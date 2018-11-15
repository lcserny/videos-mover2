package net.cserny.videosmover.service;

import net.cserny.videosmover.CoreTestComponent;
import net.cserny.videosmover.DaggerCoreTestComponent;
import net.cserny.videosmover.helper.InMemoryFileSystem;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.helper.VideoResolver;
import net.cserny.videosmover.model.Subtitle;
import net.cserny.videosmover.model.Video;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.cserny.videosmover.helper.StaticPathsProvider.getJoinedPathString;
import static org.junit.Assert.assertTrue;

public class VideoMoverTest {

    @Inject
    VideoMover videoMover;

    @Inject
    OutputResolver outputResolver;

    private InMemoryFileSystem inMemoryFileSystem;

    public VideoMoverTest() {
        CoreTestComponent component = DaggerCoreTestComponent.create();
        component.inject(this);
    }

    @Before
    public void setUp() throws Exception {
        inMemoryFileSystem = InMemoryFileSystem.initFileSystem();
    }

    @After
    public void tearDown() throws Exception {
        inMemoryFileSystem.closeFileSystem();
    }

    @Test
    public void move_multiTvShowsToCorrectPath() throws Exception {
        String gotPath = StaticPathsProvider.getDownloadsPath();
        String gotFolder = "Game.Of.Thrones.s0e10";
        String gotFile = "game.of.thrones.s07e06.720p.A.Song.Of.Ice.And.Fire.x264.mp4";

        inMemoryFileSystem.create(gotPath, gotFolder, gotFile, 2);
        Video got = VideoResolver.resolveTvShow(gotPath,
                getJoinedPathString(gotPath, gotFolder, gotFile),
                outputResolver::resolve);

        String cmPath = StaticPathsProvider.getDownloadsPath();
        String cmFolder = "Criminal.Minds.s01e01";
        String cmFile = "criminil.mids.s01e01.720p.x264.mp4";

        inMemoryFileSystem.create(cmPath, cmFolder, cmFile, 2);
        Video cm = VideoResolver.resolveTvShow(cmPath,
                getJoinedPathString(cmPath, cmFolder, cmFile),
                outputResolver::resolve);

        List<Video> videoList = Arrays.asList(got, cm);

        assertTrue(videoMover.move(videoList));
        Path movedTvShowPath1 = StaticPathsProvider.getPath(StaticPathsProvider.getTvShowsPath())
                .resolve(got.getOutputFolderWithDate());
        assertTrue(Files.exists(movedTvShowPath1));
        Path movedTvShowPath2 = StaticPathsProvider.getPath(StaticPathsProvider.getTvShowsPath())
                .resolve(cm.getOutputFolderWithDate());
        assertTrue(Files.exists(movedTvShowPath2));
    }

    @Test
    public void move_movieWithSubtitleToCorrectPathRetainingSubtitle() throws Exception {
        String bsPath = StaticPathsProvider.getDownloadsPath();
        String bsFolder = "The.Big.Sick.1080p.[2017].x264";
        String bsFile = "the.big.sick.2017.1080p.BluRay.x264.YIFY.mp4";
        String bsSubFile = "Subtitle.srt";

        inMemoryFileSystem.create(bsPath, bsFolder, bsFile, 2);
        inMemoryFileSystem.create(bsPath, getJoinedPathString(bsFolder, "Sub"), bsSubFile, 0);
        Video video = VideoResolver.resolveMovie(bsPath,
                getJoinedPathString(bsPath, bsFolder, bsFile),
                outputResolver::resolve);

        Path subtitlePath = StaticPathsProvider.getPath(getJoinedPathString(bsPath, bsFolder, "Sub", bsSubFile));
        Subtitle subtitle = new Subtitle(subtitlePath.getFileName().toString(), subtitlePath.toString());
        video.setSubtitles(Collections.singletonList(subtitle));

        assertTrue(videoMover.move(video));
        Path movedMoviePath = StaticPathsProvider.getPath(StaticPathsProvider.getMoviesPath())
                .resolve(video.getOutputFolderWithDate());
        assertTrue(Files.exists(movedMoviePath));
        Path movedMovieSubtitlePath = movedMoviePath.resolve(subtitlePath.getFileName());
        assertTrue(Files.exists(movedMovieSubtitlePath));
    }

    @Test
    public void move_subtitlesInSubsFolderThemToSubsFolderInOutputAlso() throws Exception {
        String ggPath = StaticPathsProvider.getDownloadsPath();
        String ggFolder = "the.great.gatsby.2013";
        String ggFile = "the.great.gatsby.2013.x264.1080p.avi";
        String ggSub1File = "subtitle.srt";
        String ggSub2File = "subtitle.idx";

        inMemoryFileSystem.create(ggPath, ggFolder, ggFile, 2);
        inMemoryFileSystem.create(ggPath, getJoinedPathString(ggFolder, "Subs"), ggSub1File, 0);
        inMemoryFileSystem.create(ggPath, getJoinedPathString(ggFolder, "Subs"), ggSub2File, 0);

        Video movie = VideoResolver.resolveMovie(ggPath,
                getJoinedPathString(ggPath, ggFolder, ggFile),
                outputResolver::resolve);

        Path subPath1 = StaticPathsProvider.getPath(getJoinedPathString(ggPath, ggFolder, "Subs", ggSub1File));
        Path subPath2 = StaticPathsProvider.getPath(getJoinedPathString(ggPath, ggFolder, "Subs", ggSub2File));
        movie.setSubtitles(Arrays.asList(
                new Subtitle(subPath1.getFileName().toString(), subPath1.toString()),
                new Subtitle(subPath2.getFileName().toString(), subPath2.toString())));

        assertTrue(videoMover.move(movie));

        Path sub1 = StaticPathsProvider.getPath(StaticPathsProvider.getMoviesPath())
                .resolve(movie.getOutputFolderWithDate())
                .resolve("Subs")
                .resolve(subPath1.getFileName());
        assertTrue(Files.exists(sub1));

        Path sub2 = StaticPathsProvider.getPath(StaticPathsProvider.getMoviesPath())
                .resolve(movie.getOutputFolderWithDate())
                .resolve("Subs")
                .resolve(subPath2.getFileName());
        assertTrue(Files.exists(sub2));
    }
}
