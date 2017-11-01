package net.cserny.videosmover.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.FileSystem;
import java.nio.file.Path;

/**
 * Created by leonardo on 02.09.2017.
 */
@Service
public class StaticPathsProvider {
    private static PathsInitializer pathsInitializer;

    @Autowired
    public StaticPathsProvider(PathsInitializer pathsInitializer) {
        StaticPathsProvider.pathsInitializer = pathsInitializer;
    }

    public static Path getPath(String path) {
        return pathsInitializer.getPath(path);
    }

    public static FileSystem getFileSystem() {
        return pathsInitializer.getFileSystem();
    }

    public static void setFileSystem(FileSystem fileSystem) {
        pathsInitializer.setFileSystem(fileSystem);
    }

    public static String getDownloadsPath() {
        return pathsInitializer.getDownloadsPath();
    }

    public static void setDownloadsPath(String downloadsPath) {
        pathsInitializer.setDownloadsPath(downloadsPath);
    }

    public static String getMoviesPath() {
        return pathsInitializer.getMoviesPath();
    }

    public static void setMoviesPath(String moviesPath) {
        pathsInitializer.setMoviesPath(moviesPath);
    }

    public static String getTvShowsPath() {
        return pathsInitializer.getTvShowsPath();
    }

    public static void setTvShowsPath(String tvShowsPath) {
        pathsInitializer.setTvShowsPath(tvShowsPath);
    }
}
