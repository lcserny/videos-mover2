package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.StaticPathsProvider;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MainPathsRestriction implements RemovalRestriction {

    private List<String> restrictedFolders;

    @Override
    public boolean isRestricted(Video video) {
        refreshRestrictedFolders();

        for (String restrictedFolder : restrictedFolders) {
            if (restrictedFolder.endsWith(video.getInput().getParent().getFileName().toString())) {
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
