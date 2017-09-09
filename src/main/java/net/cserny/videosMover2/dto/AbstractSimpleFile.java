package net.cserny.videosMover2.dto;

import java.nio.file.Path;

/**
 * Created by leonardo on 09.09.2017.
 */
public abstract class AbstractSimpleFile
{
    protected Path path;
    protected long size;
    protected String type;

    public Path getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
