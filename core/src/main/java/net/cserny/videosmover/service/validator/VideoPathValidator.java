package net.cserny.videosmover.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.cserny.videosmover.helper.PropertiesLoader;

import java.nio.file.Path;
import java.util.List;

@Singleton
public class VideoPathValidator implements VideoValidator {

    private List<String> excludedVideoPaths;

    @Inject
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
