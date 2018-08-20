package net.cserny.videosmover.service.validator;


import java.nio.file.Path;

/**
 * Created by leonardo on 12.09.2017.
 */
public interface RemovalRestriction  {
    boolean isRestricted(Path inputPath);
}
