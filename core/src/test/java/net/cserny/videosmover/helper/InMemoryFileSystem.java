package net.cserny.videosmover.helper;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import net.cserny.videosmover.helper.platform.Platform;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class InMemoryFileSystem {

    private FileSystem fileSystem;
    private static Platform platform;

    static {
        platform = Platform.initPlatform();
    }

    public static String getDownloads() {
        return StaticPathsProvider.getJoinedPathString(platform.getRootPathPrefix(), "Downloads");
    }

    public static String getMovies() {
        return StaticPathsProvider.getJoinedPathString(platform.getRootPathPrefix(), "Movies");
    }

    public static String getTvShows() {
        return StaticPathsProvider.getJoinedPathString(platform.getRootPathPrefix(), "TvShows");
    }

    public static String getEmpty() {
        return StaticPathsProvider.getJoinedPathString(platform.getRootPathPrefix(), "empty");
    }

    public static InMemoryFileSystem initFileSystem() throws IOException {
        InMemoryFileSystem inMemoryFileSystem = new InMemoryFileSystem();

        inMemoryFileSystem.fileSystem = Jimfs.newFileSystem(Configuration.forCurrentPlatform());
        StaticPathsProvider.setFileSystem(inMemoryFileSystem.fileSystem);

        Path downloadsFolder = inMemoryFileSystem.fileSystem.getPath(getDownloads());
        Files.createDirectory(downloadsFolder);
        StaticPathsProvider.setDownloadsPath(downloadsFolder.toString());

        Path moviesFolder = inMemoryFileSystem.fileSystem.getPath(getMovies());
        Files.createDirectory(moviesFolder);
        StaticPathsProvider.setMoviesPath(moviesFolder.toString());

        Path tvShowsFolder = inMemoryFileSystem.fileSystem.getPath(getTvShows());
        Files.createDirectory(tvShowsFolder);
        StaticPathsProvider.setTvShowsPath(tvShowsFolder.toString());

        Files.createDirectory(inMemoryFileSystem.fileSystem.getPath(getEmpty()));

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
