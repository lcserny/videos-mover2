package net.cserny.videosmover.helper;

import net.cserny.videosmover.facade.MainFacade;
import net.cserny.videosmover.model.VideoPath;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticPathsProvider {

    private static FileSystem fileSystem;
    private static String downloadsPath;
    private static String moviesPath;
    private static String tvShowsPath;

    static {
        initPaths();
    }

    private static void initPaths() {
        fileSystem = FileSystems.getDefault();

        downloadsPath = PreferencesLoader.getDownloadsPath();
        if (!Files.exists(getPath(downloadsPath))) {
            downloadsPath = null;
        }

        moviesPath = PreferencesLoader.getMoviesPath();
        if (!Files.exists(getPath(moviesPath))) {
            moviesPath = null;
        }

        tvShowsPath = PreferencesLoader.getTvShowsPath();
        if (!Files.exists(getPath(tvShowsPath))) {
            tvShowsPath = null;
        }
    }

    public static Path getPath(String path) {
        return fileSystem.getPath(path);
    }

    public static Path getPath(VideoPath videoPath) {
        return fileSystem.getPath(videoPath.getOutputPath()).resolve(MainFacade.combineOutputFolderAndYear(videoPath));
    }

    public static FileSystem getFileSystem() {
        return fileSystem;
    }

    public static void setFileSystem(FileSystem fileSystem) {
        StaticPathsProvider.fileSystem = fileSystem;
    }

    public static String getDownloadsPath() {
        return downloadsPath;
    }

    public static void setDownloadsPath(String downloadsPath) {
        StaticPathsProvider.downloadsPath = downloadsPath;
        PreferencesLoader.setDownloadsPath(downloadsPath);
    }

    public static String getMoviesPath() {
        return moviesPath;
    }

    public static void setMoviesPath(String moviesPath) {
        StaticPathsProvider.moviesPath = moviesPath;
        PreferencesLoader.setMoviesPath(moviesPath);
    }

    public static String getTvShowsPath() {
        return tvShowsPath;
    }

    public static void setTvShowsPath(String tvShowsPath) {
        StaticPathsProvider.tvShowsPath = tvShowsPath;
        PreferencesLoader.setTvShowsPath(tvShowsPath);
    }
}
