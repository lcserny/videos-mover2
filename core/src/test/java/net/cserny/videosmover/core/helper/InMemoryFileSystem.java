package net.cserny.videosmover.core.helper;

import com.google.common.jimfs.Jimfs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.jimfs.Configuration.forCurrentPlatform;
import static net.cserny.videosmover.core.helper.StaticPathsProvider.getPath;
import static net.cserny.videosmover.core.helper.StaticPathsProvider.joinPaths;
import static net.cserny.videosmover.core.helper.platform.PlatformService.getDefaultRoot;

public class InMemoryFileSystem {

    public InMemoryFileSystem() throws IOException {
        init();
        createStructure();
    }

    private void init() {
        StaticPathsProvider.setFileSystem(Jimfs.newFileSystem(forCurrentPlatform()));
        StaticPathsProvider.setDownloadsPath(getPath(joinPaths(getDefaultRoot(), "Downloads")).toString());
        StaticPathsProvider.setMoviesPath(getPath(joinPaths(getDefaultRoot(), "Movies")).toString());
        StaticPathsProvider.setTvShowsPath(getPath(joinPaths(getDefaultRoot(), "TvShows")).toString());
        StaticPathsProvider.setEmptyPath(getPath(joinPaths(getDefaultRoot(), "empty")).toString());
    }

    private void createStructure() throws IOException {
        Files.createDirectory(getPath(StaticPathsProvider.getDownloadsPath()));
        Files.createDirectory(getPath(StaticPathsProvider.getMoviesPath()));
        Files.createDirectory(getPath(StaticPathsProvider.getTvShowsPath()));
        Files.createDirectory(getPath(StaticPathsProvider.getEmptyPath()));
    }

    public void closeFileSystem() throws IOException {
        if (StaticPathsProvider.getFileSystem() != null) {
            StaticPathsProvider.getFileSystem().close();
        }
    }

    public void create(String parent, String folder, String file, int sizeInMb) throws IOException {
        Path folderPath = getPath(parent);

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
