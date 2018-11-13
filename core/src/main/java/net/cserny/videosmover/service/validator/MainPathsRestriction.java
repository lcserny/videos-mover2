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
        if (restrictedFolders == null) {
            refreshRestrictedFolders();
        }

        String inputPathString = inputPath.toString();
        if (!inputPathString.startsWith(StaticPathsProvider.getDownloadsPath())) {
            return true;
        }

        for (String restrictedFolder : restrictedFolders) {
            if (inputPathString.equalsIgnoreCase(restrictedFolder)) {
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
