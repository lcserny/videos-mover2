package net.cserny.videosmover.service.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by leonardo on 10.09.2017.
 */
@Service
@Order(1)
public class VideoPathValidator implements VideoValidator {
    @Value("#{'${video.exclude.paths}'.split(',')}")
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
