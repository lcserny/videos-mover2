package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.helper.PropertiesLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Order(0)
@Component
public class VideoPathValidator implements VideoValidator {

    private List<String> excludedVideoPaths;

    @Autowired
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
