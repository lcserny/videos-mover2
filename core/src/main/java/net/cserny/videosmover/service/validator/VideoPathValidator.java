package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.PropertiesLoader;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

public class VideoPathValidator implements VideoValidator {

    private List<String> excludedVideoPaths;

    public VideoPathValidator() {
        excludedVideoPaths = PropertiesLoader.getVideoExcludePaths();
    }

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
