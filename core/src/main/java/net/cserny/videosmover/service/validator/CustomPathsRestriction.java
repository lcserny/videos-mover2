package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.PropertiesLoader;
import net.cserny.videosmover.model.Video;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
