package net.cserny.videosMover2.service;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by leonardo on 02.09.2017.
 */
public interface VideoParser
{
    boolean isVideo(Path file) throws IOException;
}
