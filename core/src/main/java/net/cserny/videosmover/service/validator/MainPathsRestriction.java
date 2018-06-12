package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.helper.StaticPathsProvider;

import java.util.ArrayList;
import java.util.List;

public class MainPathsRestriction implements RemovalRestriction {

    private List<String> restrictedFolders;

    @Override
    public boolean isRestricted(Video video) {
        refreshRestrictedFolders();

        for (String restrictedFolder : restrictedFolders) {
            if (restrictedFolder.endsWith(video.getInputPath().getFileName().toString())) {
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
