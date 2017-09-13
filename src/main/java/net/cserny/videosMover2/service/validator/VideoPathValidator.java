package net.cserny.videosMover2.service.validator;

import net.cserny.videosMover2.service.AbstractResourceInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by leonardo on 10.09.2017.
 */
@Service
@Order(1)
public class VideoPathValidator extends AbstractResourceInitializer implements VideoValidator
{
    public static final String RESOURCE_EXCLUDEPATHS = "excluded_paths.cfg";

    private List<String> excludedVideoPaths;

    public VideoPathValidator() {
        excludedVideoPaths = fillListFromResource(RESOURCE_EXCLUDEPATHS);
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
