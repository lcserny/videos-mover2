package net.cserny.videosMover2.dto;

import java.nio.file.Path;

/**
 * Created by leonardo on 02.09.2017.
 */
public class Video
{
    private Path file;

    public Path getFile() {
        return file;
    }

    public void setFile(Path file) {
        this.file = file;
    }
}
