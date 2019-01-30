package net.cserny.videosmover.helper;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import net.cserny.videosmover.helper.platform.PlatformService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InMemoryFileSystem {

    public InMemoryFileSystem() throws IOException {
        init();
        createStructure();
    }

    private void init() {
        StaticPathsProvider.setFileSystem(Jimfs.newFileSystem(Configuration.forCurrentPlatform()));
        StaticPathsProvider.setDownloadsPath(StaticPathsProvider.joinPaths(PlatformService.getDefaultRoot(), "Downloads"));
        StaticPathsProvider.setMoviesPath(StaticPathsProvider.joinPaths(PlatformService.getDefaultRoot(), "Movies"));
        StaticPathsProvider.setTvShowsPath(StaticPathsProvider.joinPaths(PlatformService.getDefaultRoot(), "TvShows"));
        StaticPathsProvider.setEmptyPath(StaticPathsProvider.joinPaths(PlatformService.getDefaultRoot(), "empty"));
    }

    private void createStructure() throws IOException {
        Path downloadsFolder = StaticPathsProvider.getPath(StaticPathsProvider.getDownloadsPath());
        Files.createDirectory(downloadsFolder);
        StaticPathsProvider.setDownloadsPath(downloadsFolder.toString());

        Path moviesFolder = StaticPathsProvider.getPath(StaticPathsProvider.getMoviesPath());
        Files.createDirectory(moviesFolder);
        StaticPathsProvider.setMoviesPath(moviesFolder.toString());

        Path tvShowsFolder = StaticPathsProvider.getPath(StaticPathsProvider.getTvShowsPath());
        Files.createDirectory(tvShowsFolder);
        StaticPathsProvider.setTvShowsPath(tvShowsFolder.toString());

        Files.createDirectory(StaticPathsProvider.getPath(StaticPathsProvider.getEmptyPath()));
    }

    public void closeFileSystem() throws IOException {
        if (StaticPathsProvider.getFileSystem() != null) {
            StaticPathsProvider.getFileSystem().close();
        }
    }

    public void create(String parent, String folder, String file, int sizeInMb) throws IOException {
        Path folderPath = StaticPathsProvider.getPath(parent);

        if (folder != null) {
            folderPath = folderPath.resolve(folder);
            Files.createDirectories(folderPath);
        }

        if (file != null) {
            folderPath = folderPath.resolve(file);
            if (!Files.exists(folderPath)) {
                Files.createFile(folderPath);
                Files.write(folderPath, new byte[1024 * 1024 * sizeInMb]);
            }
        }
    }
}
