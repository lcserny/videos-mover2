package net.cserny.videosmover.core.helper;

import net.cserny.videosmover.core.helper.platform.PlatformService;
import net.cserny.videosmover.core.helper.platform.PlatformTrimPathData;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticPathsProvider {

    public static String SEPARATOR = PlatformService.getSeparator();

    private static FileSystem fileSystem = FileSystems.getDefault();
    private static String downloadsPath;
    private static String moviesPath;
    private static String tvShowsPath;
    private static String emptyPath = "";

    static {
        initPaths();
    }

    private static void initPaths() {
        downloadsPath = ApplicationPreferences.getDownloadsPath();
        if (!Files.exists(getPath(downloadsPath))) {
            downloadsPath = null;
        }

        moviesPath = ApplicationPreferences.getMoviesPath();
        if (!Files.exists(getPath(moviesPath))) {
            moviesPath = null;
        }

        tvShowsPath = ApplicationPreferences.getTvShowsPath();
        if (!Files.exists(getPath(tvShowsPath))) {
            tvShowsPath = null;
        }
    }

    public static Path getPath(String path, String... parts) {
        PlatformTrimPathData trimPathData = PlatformService.trimPath(new PlatformTrimPathData(path, parts));
        return fileSystem.getPath(trimPathData.getPath(), trimPathData.getParts());
    }

    public static String joinPaths(String startPath, String... paths) {
        if (StringHelper.isEmpty(startPath)) {
            throw new IllegalArgumentException("Invalid startPath argument provided");
        }

        if (startPath.length() > 1 && startPath.endsWith(SEPARATOR)) {
            startPath = startPath.substring(0, startPath.lastIndexOf(SEPARATOR));
        }

        if (paths == null || paths.length == 0) {
            return startPath;
        }

        return startPath + SEPARATOR + String.join(SEPARATOR, paths);
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
    }

    public static String getMoviesPath() {
        return moviesPath;
    }

    public static void setMoviesPath(String moviesPath) {
        StaticPathsProvider.moviesPath = moviesPath;
    }

    public static String getTvShowsPath() {
        return tvShowsPath;
    }

    public static void setTvShowsPath(String tvShowsPath) {
        StaticPathsProvider.tvShowsPath = tvShowsPath;
    }

    public static String getEmptyPath() {
        return emptyPath;
    }

    public static void setEmptyPath(String emptyPath) {
        StaticPathsProvider.emptyPath = emptyPath;
    }
}
