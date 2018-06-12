package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.helper.PropertiesLoader;
import net.cserny.videosmover.model.Video;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class CustomPathsRestriction implements RemovalRestriction {

    private List<String> restrictedFolders;

    @Inject
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
