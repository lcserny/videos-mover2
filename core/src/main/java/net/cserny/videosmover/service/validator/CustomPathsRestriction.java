package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.helper.PropertiesLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Order(1)
@Component
public class CustomPathsRestriction implements RemovalRestriction {

    private List<String> restrictedFolders;

    @Autowired
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
