package net.cserny.videosMover.service;


import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by leonardo on 02.09.2017.
 */
public interface SubtitlesFinder {
    List<Path> find(Path file) throws IOException;
}
