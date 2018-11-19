package net.cserny.videosmover.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.cserny.videosmover.helper.PropertiesLoader;

import java.nio.file.Path;
import java.util.List;

@Singleton
public class CustomPathsRestriction implements RemovalRestriction {

    private List<String> restrictedFolders;

    @Inject
    public CustomPathsRestriction() {
        restrictedFolders = PropertiesLoader.getRestrictedFolders();
    }

    @Override
    public boolean isRestricted(Path inputFolderPath) {
        for (String restrictedFolder : restrictedFolders) {
            if (inputFolderPath.getFileName().toString().equals(restrictedFolder)) {
                return true;
            }
        }
        return false;
    }
}
