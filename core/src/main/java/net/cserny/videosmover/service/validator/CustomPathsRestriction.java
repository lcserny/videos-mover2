package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.helper.PropertiesLoader;
import net.cserny.videosmover.model.Video;

import java.util.List;

public class CustomPathsRestriction implements RemovalRestriction {

    private List<String> restrictedFolders;

    public CustomPathsRestriction() {
        restrictedFolders = PropertiesLoader.getRestrictedFolders();
    }

    @Override
    public boolean isRestricted(Video video) {
        for (String restrictedFolder : restrictedFolders) {
            if (video.getInputPath().getFileName().toString().equals(restrictedFolder)) {
                return true;
            }
        }
        return false;
    }
}
