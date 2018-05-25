package net.cserny.videosmover.service;

import net.cserny.videosmover.PropertiesLoader;

import javax.annotation.PostConstruct;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class PathsInitializer {

    private String downloadsPath;
    private String moviesPath;
    private String tvShowsPath;
    private FileSystem fileSystem;

    public PathsInitializer() {
        downloadsPath = PropertiesLoader.getDownloadsPath();
        moviesPath = PropertiesLoader.getMoviesPath();
        tvShowsPath = PropertiesLoader.getTvShowsPath();
        fileSystem = FileSystems.getDefault();

        init();
    }

    private void init() {
        if (downloadsPath == null || !Files.exists(getPath(downloadsPath))) {
            downloadsPath = null;
        }
        if (moviesPath == null || !Files.exists(getPath(moviesPath))) {
            moviesPath = null;
        }
        if (tvShowsPath == null || !Files.exists(getPath(tvShowsPath))) {
            tvShowsPath = null;
        }
    }

    public Path getPath(String path) {
        return fileSystem.getPath(path);
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public String getDownloadsPath() {
        return downloadsPath;
    }

    public void setDownloadsPath(String downloadsPath) {
        this.downloadsPath = downloadsPath;
    }

    public String getMoviesPath() {
        return moviesPath;
    }

    public void setMoviesPath(String moviesPath) {
        this.moviesPath = moviesPath;
    }

    public String getTvShowsPath() {
        return tvShowsPath;
    }

    public void setTvShowsPath(String tvShowsPath) {
        this.tvShowsPath = tvShowsPath;
    }
}
