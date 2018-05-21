package net.cserny.videosmover.service;

import java.nio.file.FileSystem;
import java.nio.file.Path;

public class StaticPathsProvider {

    public static PathsInitializer pathsInitializer;

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
