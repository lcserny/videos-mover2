package net.cserny.videosmover.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Created by leonardo on 02.09.2017.
 */
public class PathsProvider {
    private static final Logger logger = LoggerFactory.getLogger(PathsProvider.class);

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
        Properties properties = new Properties();
        try (InputStream input = PathsProvider.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
        } catch (IOException e) {
            logger.error("Cannot load application.properties");
        }

        String pathDownloadsProperty = properties.getProperty("path.downloads");
        if (pathDownloadsProperty != null && Files.exists(getPath(pathDownloadsProperty))) {
            downloadsPath = pathDownloadsProperty;
        }

        String pathMoviesProperty = properties.getProperty("path.movies");
        if (pathMoviesProperty != null && Files.exists(getPath(pathMoviesProperty))) {
            moviesPath = pathMoviesProperty;
        }

        String pathTvShowsProperty = properties.getProperty("path.tvshows");
        if (pathTvShowsProperty != null && Files.exists(getPath(pathTvShowsProperty))) {
            tvShowsPath = pathTvShowsProperty;
        }
    }
}
