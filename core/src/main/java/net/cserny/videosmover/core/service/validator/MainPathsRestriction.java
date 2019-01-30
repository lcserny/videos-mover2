package net.cserny.videosmover.core.service.validator;

import net.cserny.videosmover.core.helper.StaticPathsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Order(0)
@Component
public class MainPathsRestriction implements RemovalRestriction {

    private List<String> restrictedFolders;

    @Autowired
    public MainPathsRestriction() {
    }

    @Override
    public boolean isRestricted(Path inputFolderPath) {
        if (restrictedFolders == null) {
            refreshRestrictedFolders();
        }

        String inputPathString = inputFolderPath.toString();
        if (!inputPathString.startsWith(StaticPathsProvider.getDownloadsPath())) {
            return true;
        }

        for (String restrictedFolder : restrictedFolders) {
            if (inputPathString.equals(restrictedFolder)) {
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
