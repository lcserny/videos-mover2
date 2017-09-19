package net.cserny.videosmover.service.validator;


import net.cserny.videosmover.model.Video;

/**
 * Created by leonardo on 12.09.2017.
 */
public interface RemovalRestriction  {
    boolean isRestricted(Video video);
}
