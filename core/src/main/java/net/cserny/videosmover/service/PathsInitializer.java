package net.cserny.videosmover.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Service
public class PathsInitializer {
    @Value("${path.downloads}")
    private String downloadsPath;
    @Value("${path.movies}")
    private String moviesPath;
    @Value("${path.tvshows}")
    private String tvShowsPath;

    private FileSystem fileSystem;

    public PathsInitializer() {
        fileSystem = FileSystems.getDefault();
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
