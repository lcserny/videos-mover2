package net.cserny.videosmover.helper;

import net.cserny.videosmover.facade.MainFacade;
import net.cserny.videosmover.model.VideoPath;

import java.io.File;
import java.nio.file.*;

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

    public static Path getPath(String path, String... parts) {
        path = path.trim();
        boolean invalidParts = false;
        if (parts != null) {
            for (String part : parts) {
                if (part.isEmpty()) {
                    invalidParts = true;
                    break;
                }
            }
        }
        if (invalidParts) {
            return fileSystem.getPath(path);
        }
        return fileSystem.getPath(path, parts);
    }

    public static String getPathString(boolean appendRootSlash, String... paths) {
        String fullPath = String.join(File.separator, paths);
        if (appendRootSlash) {
            fullPath = File.separator + fullPath;
        }
        return fullPath;
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
