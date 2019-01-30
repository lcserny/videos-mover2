package net.cserny.videosmover.helper;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import net.cserny.videosmover.helper.platform.PlatformService;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class InMemoryFileSystem {

    private FileSystem fileSystem;
    private String downloads;
    private String movies;
    private String tvShows;
    private String empty;

    public InMemoryFileSystem() throws IOException {
        fileSystem = Jimfs.newFileSystem(Configuration.forCurrentPlatform());
        StaticPathsProvider.setFileSystem(fileSystem);

        downloads = StaticPathsProvider.joinPaths(PlatformService.getDefaultRoot(), "Downloads");
        movies = StaticPathsProvider.joinPaths(PlatformService.getDefaultRoot(), "Movies");
        tvShows = StaticPathsProvider.joinPaths(PlatformService.getDefaultRoot(), "TvShows");
        empty = StaticPathsProvider.joinPaths(PlatformService.getDefaultRoot(), "empty");

        Path downloadsFolder = fileSystem.getPath(downloads);
        Files.createDirectory(downloadsFolder);
        StaticPathsProvider.setDownloadsPath(downloadsFolder.toString());

        Path moviesFolder = fileSystem.getPath(movies);
        Files.createDirectory(moviesFolder);
        StaticPathsProvider.setMoviesPath(moviesFolder.toString());

        Path tvShowsFolder = fileSystem.getPath(tvShows);
        Files.createDirectory(tvShowsFolder);
        StaticPathsProvider.setTvShowsPath(tvShowsFolder.toString());

        Files.createDirectory(fileSystem.getPath(empty));
    }

    public String getDownloads() {
        return downloads;
    }

    public String getMovies() {
        return movies;
    }

    public String getTvShows() {
        return tvShows;
    }

    public String getEmpty() {
        return empty;
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
