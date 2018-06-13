package net.cserny.videosmover.helper;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class InMemoryVideoFileSystemInitializer {

    public static final String DOWNLOADS = "/Downloads/";
    public static final String DOWNLOADS_EMPTY_FOLDER = DOWNLOADS + "emptyFolder";
    public static final String DOWNLOADS_REGULAR_FILE = DOWNLOADS + "NonVideoFolder/NonVideo.txt";
    public static final String DOWNLOADS_ILLEGAL_VIDEO = DOWNLOADS + "Programming Stuff/illegalVideo.mp4";
    public static final String DOWNLOADS_SMALL_VIDEO = DOWNLOADS + "SmallVideoFolder/smallVideo.mp4";
    public static final String DOWNLOADS_ROOT_VIDEO = DOWNLOADS + "fromDownloads.mp4";
    public static final String DOWNLOADS_BIGSICK = DOWNLOADS + "The.Big.Sick.1080p.[2017].x264/";
    public static final String DOWNLOADS_MOVIE_WITH_SUBTITLE = DOWNLOADS_BIGSICK + "the.big.sick.2017.1080p.BluRay.x264.YIFY.mp4";
    public static final String DOWNLOADS_SUBTITLE = DOWNLOADS_BIGSICK + "Sub/Subtitle.srt";
    public static final String DOWNLOADS_RESTRICTED_MOVIE = DOWNLOADS + "The.Hero.1080p.[2017].x264/the.hero.2017.1080p.BluRay.x264.YIFY.mp4";
    public static final String DOWNLOADS_MOVIE_ALL_DIGITS = DOWNLOADS + "1922.1080p.[2017].x264/1922.2017.1080p.BluRay.x264.YIFY.mp4";
    public static final String DOWNLOADS_EXISTING_TVSHOW = DOWNLOADS + "Criminal.Minds.s01e01/criminil.mids.s01e01.720p.x264.mp4";
    public static final String DOWNLOADS_TVSHOW = DOWNLOADS + "Game.Of.Thrones.s0e10/game.of.thrones.s07e06.720p.A.Song.Of.Ice.And.Fire.x264.mp4";
    public static final String DOWNLOADS_GATSBY = DOWNLOADS + "the.great.gatsby.2013/";
    public static final String DOWNLOADS_MOVIE_WITH_SUBTITLE_IN_SUBS = DOWNLOADS_GATSBY + "the.great.gatsby.2013.x264.1080p.avi";
    public static final String DOWNLOADS_SUBTITLE_IN_SUBS = DOWNLOADS_GATSBY + "Subs/subtitle.srt";
    public static final String DOWNLOADS_SUBTITLE_IN_SUBS_IDX = DOWNLOADS_GATSBY + "Subs/subtitle.idx";

    protected FileSystem inMemoryFilesystem;
    private Path downloadsFolder;
    private Path moviesFolder;
    private Path tvShowsFolder;

    public void setUp() throws Exception {
        setupInMemoryFolders();
        createTestFiles();
    }

    public void tearDown() throws Exception {
        inMemoryFilesystem.close();
    }

    private void createTestFiles() throws IOException {
        createFolder(downloadsFolder, "emptyFolder");
        createFile(downloadsFolder, "NonVideoFolder", "NonVideo.txt", 0);
        createFile(downloadsFolder, "Programming Stuff", "illegalVideo.mp4", 2);
        createFile(downloadsFolder, "SmallVideoFolder", "smallVideo.mp4", 1);
        createFile(downloadsFolder, null, "fromDownloads.mp4", 2);
        createFile(downloadsFolder, "The.Big.Sick.1080p.[2017].x264", "the.big.sick.2017.1080p.BluRay.x264.YIFY.mp4", 2);
        createFile(downloadsFolder, "The.Big.Sick.1080p.[2017].x264/Sub", "Subtitle.srt", 0);
        createFile(downloadsFolder, "The.Hero.1080p.[2017].x264", "the.hero.2017.1080p.BluRay.x264.YIFY.mp4", 2);
        createFile(downloadsFolder, "1922.1080p.[2017].x264", "1922.2017.1080p.BluRay.x264.YIFY.mp4", 2);
        createFile(downloadsFolder, "Game.Of.Thrones.s0e10", "game.of.thrones.s07e06.720p.A.Song.Of.Ice.And.Fire.x264.mp4", 2);
        createFile(downloadsFolder, "Criminal.Minds.s01e01", "criminil.mids.s01e01.720p.x264.mp4", 2);
        createFolder(tvShowsFolder, "Criminal Minds");
        createFile(downloadsFolder, "the.great.gatsby.2013", "the.great.gatsby.2013.x264.1080p.avi", 2);
        createFile(downloadsFolder, "the.great.gatsby.2013/Subs", "subtitle.srt", 0);
        createFile(downloadsFolder, "the.great.gatsby.2013/Subs", "subtitle.idx", 0);
    }

    private void setupInMemoryFolders() throws IOException {
        inMemoryFilesystem = Jimfs.newFileSystem(Configuration.forCurrentPlatform());
        StaticPathsProvider.setFileSystem(inMemoryFilesystem);

        downloadsFolder = inMemoryFilesystem.getPath("/Downloads");
        Files.createDirectory(downloadsFolder);
        StaticPathsProvider.setDownloadsPath(downloadsFolder.toString());

        moviesFolder = inMemoryFilesystem.getPath("/Movies");
        Files.createDirectory(moviesFolder);
        StaticPathsProvider.setMoviesPath(moviesFolder.toString());

        tvShowsFolder = inMemoryFilesystem.getPath("/TvShows");
        Files.createDirectory(tvShowsFolder);
        StaticPathsProvider.setTvShowsPath(tvShowsFolder.toString());

        Files.createDirectory(inMemoryFilesystem.getPath("/empty"));
    }

    private Path createFolder(Path root, String folderName) throws IOException {
        Path folderPath = folderName != null ? root.resolve(folderName) : root;
        if (folderName != null && !Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }
        return folderPath;
    }

    private Path createFile(Path root, String folderName, String fileName, int sizeInMb) throws IOException {
        Path folderPath = createFolder(root, folderName);
        Path file = folderPath.resolve(fileName);
        if (!Files.exists(file)) {
            Files.createFile(file);
            Files.write(file, new byte[1024 * 1024 * sizeInMb]);
        }
        return file;
    }
}
