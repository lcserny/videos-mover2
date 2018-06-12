package net.cserny.videosmover.helper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
public class PathsInitializer {

    private final PreferencesLoader preferencesLoader;

    private String downloadsPath;
    private String moviesPath;
    private String tvShowsPath;
    private FileSystem fileSystem;

    @Inject
    public PathsInitializer(PreferencesLoader preferencesLoader) {
        this.preferencesLoader = preferencesLoader;

        downloadsPath = preferencesLoader.getDownloadsPath();
        moviesPath = preferencesLoader.getMoviesPath();
        tvShowsPath = preferencesLoader.getTvShowsPath();
        fileSystem = FileSystems.getDefault();

        init();
    }

    private void init() {
        if (!Files.exists(getPath(downloadsPath))) {
            downloadsPath = null;
        }
        if (!Files.exists(getPath(moviesPath))) {
            moviesPath = null;
        }
        if (!Files.exists(getPath(tvShowsPath))) {
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
        preferencesLoader.setDownloadsPath(downloadsPath);
    }

    public String getMoviesPath() {
        return moviesPath;
    }

    public void setMoviesPath(String moviesPath) {
        this.moviesPath = moviesPath;
        preferencesLoader.setMoviesPath(moviesPath);
    }

    public String getTvShowsPath() {
        return tvShowsPath;
    }

    public void setTvShowsPath(String tvShowsPath) {
        this.tvShowsPath = tvShowsPath;
        preferencesLoader.setTvShowsPath(tvShowsPath);
    }
}
