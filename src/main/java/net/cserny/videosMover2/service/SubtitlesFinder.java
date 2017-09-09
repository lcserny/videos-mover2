package net.cserny.videosMover2.service;

import net.cserny.videosMover2.dto.AbstractSimpleFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by leonardo on 02.09.2017.
 */
public interface SubtitlesFinder
{
    List<AbstractSimpleFile> find(Path file) throws IOException;
}
