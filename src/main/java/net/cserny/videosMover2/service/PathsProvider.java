package net.cserny.videosMover2.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by leonardo on 02.09.2017.
 */
public class PathsProvider
{
    private static FileSystem fileSystem;
    private static String downloadsPath;
    private static String moviesPath;
    private static String tvShowsPath;

    static {
        fileSystem = FileSystems.getDefault();
        initDefaults();
    }

    public static Path getPath(String path) {
        return fileSystem.getPath(path);
    }

    public static FileSystem getFileSystem() {
        return fileSystem;
    }

    public static void setFileSystem(FileSystem fileSystem) {
        PathsProvider.fileSystem = fileSystem;
    }

    public static String getDownloadsPath() {
        return downloadsPath;
    }

    public static void setDownloadsPath(String downloadsPath) {
        PathsProvider.downloadsPath = downloadsPath;
    }

    public static String getMoviesPath() {
        return moviesPath;
    }

    public static void setMoviesPath(String moviesPath) {
        PathsProvider.moviesPath = moviesPath;
    }

    public static String getTvShowsPath() {
        return tvShowsPath;
    }

    public static void setTvShowsPath(String tvShowsPath) {
        PathsProvider.tvShowsPath = tvShowsPath;
    }

    private static void initDefaults() {
        if (isWindowsOs()) {
            initPaths("D:/");
        } else {
            if (isLaptopWithHostname("ubulap")) {
                initPaths("/home/sabyx/");
            } else {
                initPaths("/mnt/Data/");
            }
        }
    }

    private static void initPaths(String osPrefix) {
        String tmpDownloadsPath = osPrefix + "Downloads";
        if (Files.exists(getPath(tmpDownloadsPath))) {
            downloadsPath = tmpDownloadsPath;
        }

        String tmpMoviesPath = osPrefix + "Movies/Movies";
        String tmpAlternateMoviesPath = osPrefix + "Videos/Movies";
        if (Files.exists(getPath(tmpMoviesPath))) {
            moviesPath = tmpMoviesPath;
        } else if (Files.exists(getPath(tmpAlternateMoviesPath))) {
            moviesPath = tmpAlternateMoviesPath;
        }

        String tmpTvShowsPath = osPrefix + "Movies/TV";
        String tmpTlternateTvShowsPath = osPrefix + "Videos/TV";
        if (Files.exists(getPath(tmpTvShowsPath))) {
            tvShowsPath = tmpTvShowsPath;
        } else if (Files.exists(getPath(tmpTlternateTvShowsPath))) {
            tvShowsPath = tmpTlternateTvShowsPath;
        }
    }

    private static boolean isLaptopWithHostname(String hostname) {
        try {
            return hostname.equals(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException ignored) { }
        return false;
    }

    private static boolean isWindowsOs() {
        String osName = System.getProperty("os.name", "generic").toLowerCase();
        return osName.contains("win");
    }


}
