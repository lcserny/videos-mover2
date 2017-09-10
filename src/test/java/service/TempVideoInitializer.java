package service;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import net.cserny.videosMover2.service.PathsProvider;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by leonardo on 09.09.2017.
 */
public class TempVideoInitializer
{
    public static final String DOWNLOADS_EMPTY_FOLDER = "/Downloads/emptyFolder";
    public static final String DOWNLOADS_REGULAR_FILE = "/Downloads/NonVideoFolder/NonVideo.txt";
    public static final String DOWNLOADS_ILLEGAL_VIDEO = "/Downloads/Programming Stuff/illegalVideo.mp4";
    public static final String DOWNLOADS_SMALL_VIDEO = "/Downloads/SmallVideoFolder/smallVideo.mp4";
    public static final String DOWNLOADS_ROOT_VIDEO = "/Downloads/fromDownloads.mp4";
    public static final String DOWNLOADS_MOVIE_WITH_SUBTITLE = "/Downloads/The.Big.Sick.1080p.[2017].x264/the.big.sick.2017.1080p.BluRay.x264.YIFY.mp4";
    public static final String DOWNLOADS_SUBTITLE = "/Downloads/The.Big.Sick.1080p.[2017].x264/Sub/Subtitle.srt";
    public static final String DOWNLOADS_EXISTING_TVSHOW = "/Downloads/Criminal.Minds.s01e01/criminil.mids.s01e01.720p.x264.mp4";
    public static final String DOWNLOADS_TVSHOW = "/Downloads/Game.Of.Thrones.s0e10/game.of.thrones.720p.A.Song.Of.Ice.And.Fire.x264.mp4";

    protected FileSystem inMemoryFilesystem;
    private Path downloadsFolder;
    private Path moviesFolder;
    private Path tvShowsFolder;

    @Before
    public void setUp() throws Exception {
        setupInMemoryFolders();
        createTestFiles();
    }

    @After
    public void tearDown() throws Exception {
        inMemoryFilesystem.close();
    }

    private void createTestFiles() throws Exception {
        createFolder(downloadsFolder, "emptyFolder");

        createFile(downloadsFolder, "NonVideoFolder", "NonVideo.txt", 0);

        createFile(downloadsFolder, "Programming Stuff", "illegalVideo.mp4", 60);

        createFile(downloadsFolder, "SmallVideoFolder", "smallVideo.mp4", 5);

        createFile(downloadsFolder, null, "fromDownloads.mp4", 60);

        createFile(downloadsFolder, "The.Big.Sick.1080p.[2017].x264", "the.big.sick.2017.1080p.BluRay.x264.YIFY.mp4", 60);
        createFile(downloadsFolder, "The.Big.Sick.1080p.[2017].x264/Sub", "Subtitle.srt", 0);

        createFile(downloadsFolder, "Game.Of.Thrones.s0e10", "game.of.thrones.720p.A.Song.Of.Ice.And.Fire.x264.mp4", 60);

        createFile(downloadsFolder, "Criminal.Minds.s01e01", "criminil.mids.s01e01.720p.x264.mp4", 60);
        createFolder(tvShowsFolder, "Criminal Minds");
    }

    private void setupInMemoryFolders() throws IOException {
        inMemoryFilesystem = Jimfs.newFileSystem(Configuration.forCurrentPlatform());
        PathsProvider.setFileSystem(inMemoryFilesystem);

        downloadsFolder = inMemoryFilesystem.getPath("/Downloads");
        Files.createDirectory(downloadsFolder);
        PathsProvider.setDownloadsPath(downloadsFolder.toString());

        moviesFolder = inMemoryFilesystem.getPath("/Movies");
        Files.createDirectory(moviesFolder);
        PathsProvider.setMoviesPath(moviesFolder.toString());

        tvShowsFolder = inMemoryFilesystem.getPath("/TvShows");
        Files.createDirectory(tvShowsFolder);
        PathsProvider.setTvShowsPath(tvShowsFolder.toString());
    }

    private void createFolder(Path root, String folderName) throws IOException {
        Path folderPath = folderName != null ? root.resolve(folderName) : root;
        if (folderName != null && !Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }
    }

    private void createFile(Path root, String folder, String fileName, int sizeInMb) throws Exception {
        Path folderPath = folder != null ? root.resolve(folder) : root;
        if (folder != null && !Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        Path file = folderPath.resolve(fileName);
        if (!Files.exists(file)) {
            Files.createFile(file);
            Files.write(file, new byte[1024 * 1024 * sizeInMb]);
        }
    }
}
