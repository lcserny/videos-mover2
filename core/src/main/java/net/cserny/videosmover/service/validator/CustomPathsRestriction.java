package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.PropertiesLoader;
import net.cserny.videosmover.model.Video;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

@Singleton
public class CustomPathsRestriction implements RemovalRestriction {

    private List<String> restrictedFolders;

    public CustomPathsRestriction() {
        restrictedFolders = PropertiesLoader.getRestrictedFolders();
    }

    @Override
    public boolean isRestricted(Video video) {
        for (String restrictedFolder : restrictedFolders) {
            if (video.getInput().getParent().getFileName().toString().equals(restrictedFolder)) {
                return true;
            }
        }
        return false;
    }
}
