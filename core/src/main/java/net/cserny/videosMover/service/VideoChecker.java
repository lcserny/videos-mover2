package net.cserny.videosMover.service;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by leonardo on 02.09.2017.
 */
public interface VideoChecker {
    boolean isVideo(Path file) throws IOException;
}
