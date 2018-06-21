package net.cserny.videosmover.service;

import net.cserny.videosmover.CoreTestComponent;
import net.cserny.videosmover.DaggerCoreTestComponent;
import net.cserny.videosmover.helper.InMemoryFileSystem;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.helper.VideoResolver;
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
        Video got = VideoResolver.resolveTvShow(String.join("/", gotPath, gotFolder, gotFile),
                outputResolver::resolve);

        String cmPath = StaticPathsProvider.getDownloadsPath();
        String cmFolder = "Criminal.Minds.s01e01";
        String cmFile = "criminil.mids.s01e01.720p.x264.mp4";

        inMemoryFileSystem.create(cmPath, cmFolder, cmFile, 2);
        Video cm = VideoResolver.resolveTvShow(String.join("/", cmPath, cmFolder, cmFile),
                outputResolver::resolve);

        List<Video> videoList = Arrays.asList(got, cm);

        assertTrue(videoMover.move(videoList));
        Path movedTvShowPath1 = StaticPathsProvider.getPath(StaticPathsProvider.getTvShowsPath())
                .resolve(got.getOutputPath()).resolve(got.getOutputFolderName());
        assertTrue(Files.exists(movedTvShowPath1));
        Path movedTvShowPath2 = StaticPathsProvider.getPath(StaticPathsProvider.getTvShowsPath())
                .resolve(cm.getOutputPath()).resolve(cm.getOutputFolderName());
        assertTrue(Files.exists(movedTvShowPath2));
    }

    @Test
    public void move_movieWithSubtitleToCorrectPathRetainingSubtitle() throws Exception {
        String bsPath = StaticPathsProvider.getDownloadsPath();
        String bsFolder = "The.Big.Sick.1080p.[2017].x264";
        String bsFile = "the.big.sick.2017.1080p.BluRay.x264.YIFY.mp4";
        String bsSubFile = "Subtitle.srt";

        inMemoryFileSystem.create(bsPath, bsFolder, bsFile, 2);
        inMemoryFileSystem.create(bsPath, bsFolder + "/Sub", bsSubFile, 0);
        Video video = VideoResolver.resolveMovie(String.join("/", bsPath, bsFolder, bsFile),
                outputResolver::resolve);

        Path subtitlePath = StaticPathsProvider.getPath(String.join("/", bsPath, bsFolder, "Sub", bsSubFile));
        video.setSubtitles(Collections.singletonList(subtitlePath));

        assertTrue(videoMover.move(video));
        Path movedMoviePath = StaticPathsProvider.getPath(StaticPathsProvider.getMoviesPath())
                .resolve(video.getOutputPath()).resolve(video.getOutputFolderName());
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
        inMemoryFileSystem.create(ggPath, ggFolder + "/Subs", ggSub1File, 0);
        inMemoryFileSystem.create(ggPath, ggFolder + "/Subs", ggSub2File, 0);

        Video video = VideoResolver.resolveMovie(String.join("/", ggPath, ggFolder,ggFile),
                outputResolver::resolve);
        Path subPath1 = StaticPathsProvider.getPath(String.join("/", ggPath, ggFolder, "Subs", ggSub1File));
        Path subPath2 = StaticPathsProvider.getPath(String.join("/", ggPath, ggFolder, "Subs", ggSub2File));
        video.setSubtitles(Arrays.asList(subPath1, subPath2));

        assertTrue(videoMover.move(video));
        assertTrue(Files.exists(video.getOutputPath().resolve(video.getOutputFolderName())
                .resolve("Subs").resolve(subPath1.getFileName())));
        assertTrue(Files.exists(video.getOutputPath().resolve(video.getOutputFolderName())
                .resolve("Subs").resolve(subPath2.getFileName())));
    }
}
