package net.cserny.videosmover.service.validator;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by leonardo on 10.09.2017.
 */
public interface VideoValidator {
    boolean isValid(Path file);
}
