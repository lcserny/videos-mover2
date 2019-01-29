package net.cserny.videosmover.service.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

import static net.cserny.videosmover.constants.PropertyConstants.RESTRICTED_REMOVE_PATHS_KEY;

@Order(1)
@Component
public class CustomPathsRestriction implements RemovalRestriction {

    @Value("#{'${" + RESTRICTED_REMOVE_PATHS_KEY + "}'.split(',')}")
    private List<String> restrictedFolders;

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
