package net.cserny.videosMover.service.validator;

import net.cserny.videosMover.model.Video;
import net.cserny.videosMover.service.PathsProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardo on 12.09.2017.
 */
@Service
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
        restrictedFolders.add(PathsProvider.getDownloadsPath());
        restrictedFolders.add(PathsProvider.getTvShowsPath());
        restrictedFolders.add(PathsProvider.getMoviesPath());
    }
}
