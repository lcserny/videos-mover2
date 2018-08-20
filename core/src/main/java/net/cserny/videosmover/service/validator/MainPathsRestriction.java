package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.helper.StaticPathsProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MainPathsRestriction implements RemovalRestriction {

    private List<String> restrictedFolders;

    @Inject
    public MainPathsRestriction() { }

    @Override
    public boolean isRestricted(Path inputPath) {
        refreshRestrictedFolders();

        for (String restrictedFolder : restrictedFolders) {
            if (restrictedFolder.endsWith(inputPath.getFileName().toString())) {
                return true;
            }
        }
        return false;
    }

    private void refreshRestrictedFolders() {
        restrictedFolders = new ArrayList<>();
        restrictedFolders.add(StaticPathsProvider.getDownloadsPath());
        restrictedFolders.add(StaticPathsProvider.getTvShowsPath());
        restrictedFolders.add(StaticPathsProvider.getMoviesPath());
    }
}
