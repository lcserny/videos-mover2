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

    public InMemoryFileSystem() throws IOException {
        fileSystem = Jimfs.newFileSystem(Configuration.forCurrentPlatform());
        StaticPathsProvider.setFileSystem(fileSystem);

        Path downloadsFolder = fileSystem.getPath(getDownloads());
        Files.createDirectory(downloadsFolder);
        StaticPathsProvider.setDownloadsPath(downloadsFolder.toString());

        Path moviesFolder = fileSystem.getPath(getMovies());
        Files.createDirectory(moviesFolder);
        StaticPathsProvider.setMoviesPath(moviesFolder.toString());

        Path tvShowsFolder = fileSystem.getPath(getTvShows());
        Files.createDirectory(tvShowsFolder);
        StaticPathsProvider.setTvShowsPath(tvShowsFolder.toString());

        Files.createDirectory(fileSystem.getPath(getEmpty()));
    }

    private String getDownloads() {
        return StaticPathsProvider.joinPaths(PlatformService.getDefaultRoot(), "Downloads");
    }

    private String getMovies() {
        return StaticPathsProvider.joinPaths(PlatformService.getDefaultRoot(), "Movies");
    }

    private String getTvShows() {
        return StaticPathsProvider.joinPaths(PlatformService.getDefaultRoot(), "TvShows");
    }

    public String getEmpty() {
        return StaticPathsProvider.joinPaths(PlatformService.getDefaultRoot(), "empty");
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
