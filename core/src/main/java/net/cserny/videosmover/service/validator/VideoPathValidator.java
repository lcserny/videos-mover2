package net.cserny.videosmover.service.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

import static net.cserny.videosmover.constants.PropertyConstants.VIDEO_EXCLUDE_PATHS_KEY;

@Order(0)
@Component
public class VideoPathValidator implements VideoValidator {

    @Value("#{'${" + VIDEO_EXCLUDE_PATHS_KEY + "}'.split(',')}")
    private List<String> excludedVideoPaths;

    @Override
    public boolean isValid(Path file) {
        String pathString = file.getParent().toString();
        return pathIsAllowed(pathString);
    }

    private boolean pathIsAllowed(String pathString) {
        for (String excludePath : excludedVideoPaths) {
            if (pathString.contains(excludePath)) {
                return false;
            }
        }
        return true;
    }
}
