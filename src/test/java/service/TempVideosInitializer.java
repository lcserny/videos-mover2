package service;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import net.cserny.videosMover2.service.SystemPathsProvider;
import org.junit.Before;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by leonardo on 09.09.2017.
 */
// TODO: everyone extend this, needed for test environment
public abstract class TempVideosInitializer
{
    private Path downloadsFolder;
    private Path moviesFolder;
    private Path tvShowsFolder;

    @Before
    public void setUp() throws Exception {
        setupInMemoryFolders();
        createTestFiles();
    }

    // TODO: create files needed, as in TestVideosProvider.class
    private void createTestFiles() throws Exception {
        createFile(downloadsFolder, "The Big Sick (2017)", "theMovie.mp4", 60);
        createFile(downloadsFolder, "The Big Sick (2017)/Something/Subs", "subtitle.txt", 0);
    }

    private void setupInMemoryFolders() throws IOException {
        FileSystem inMemoryFilesystem = Jimfs.newFileSystem(Configuration.forCurrentPlatform());

        downloadsFolder = inMemoryFilesystem.getPath("/Downloads");
        Files.createDirectory(downloadsFolder);
        SystemPathsProvider.setDownloadsPath(downloadsFolder.toString());

        moviesFolder = inMemoryFilesystem.getPath("/Movies");
        Files.createDirectory(moviesFolder);
        SystemPathsProvider.setMoviesPath(moviesFolder.toString());

        tvShowsFolder = inMemoryFilesystem.getPath("/TvShows");
        Files.createDirectory(tvShowsFolder);
        SystemPathsProvider.setTvShowsPath(tvShowsFolder.toString());
    }

    private Path createFile(Path root, String folder, String fileName, int sizeInMb) throws Exception {
        Path folderPath = root.resolve(folder);
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        Path file = folderPath.resolve(fileName);
        if (!Files.exists(file)) {
            Files.createFile(file);
            Files.write(file, new byte[1024 * 1024 * sizeInMb]);
        }

        return file;
    }
}
