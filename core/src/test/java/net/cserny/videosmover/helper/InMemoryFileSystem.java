package net.cserny.videosmover.helper;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class InMemoryFileSystem {

    private static final String DOWNLOADS = "/Downloads";
    private static final String MOVIES = "/Movies";
    private static final String TV_SHOWS = "/TvShows";
    private static final String EMPTY = "/empty";

    private FileSystem fileSystem;

    private InMemoryFileSystem() { }

    public static InMemoryFileSystem initFileSystem() throws IOException {
        InMemoryFileSystem inMemoryFileSystem = new InMemoryFileSystem();

        inMemoryFileSystem.fileSystem = Jimfs.newFileSystem(Configuration.forCurrentPlatform());
        StaticPathsProvider.setFileSystem(inMemoryFileSystem.fileSystem);

        Path downloadsFolder = inMemoryFileSystem.fileSystem.getPath(DOWNLOADS);
        Files.createDirectory(downloadsFolder);
        StaticPathsProvider.setDownloadsPath(downloadsFolder.toString());

        Path moviesFolder = inMemoryFileSystem.fileSystem.getPath(MOVIES);
        Files.createDirectory(moviesFolder);
        StaticPathsProvider.setMoviesPath(moviesFolder.toString());

        Path tvShowsFolder = inMemoryFileSystem.fileSystem.getPath(TV_SHOWS);
        Files.createDirectory(tvShowsFolder);
        StaticPathsProvider.setTvShowsPath(tvShowsFolder.toString());

        Files.createDirectory(inMemoryFileSystem.fileSystem.getPath(EMPTY));

        return inMemoryFileSystem;
    }

    public void closeFileSystem() throws IOException {
        if (fileSystem != null) {
            fileSystem.close();
        }
    }

    public void create(String parent, String folder, String file, int sizeInMb) throws IOException {
        Path folderPath = StaticPathsProvider.getPath(parent);

        if (folder != null) {
            folderPath = folderPath.resolve(folder);
            Files.createDirectories(folderPath);
        }

        if (file != null) {
            folderPath = folderPath.resolve(file);
            if (!Files.exists(folderPath)) {
                Files.createFile(folderPath);
                Files.write(folderPath, new byte[1024 * 1024 * sizeInMb]);
            }
        }
    }
}
