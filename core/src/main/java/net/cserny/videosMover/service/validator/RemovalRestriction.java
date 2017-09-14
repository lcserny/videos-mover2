package net.cserny.videosMover.service.validator;


import net.cserny.videosMover.model.Video;

/**
 * Created by leonardo on 12.09.2017.
 */
public interface RemovalRestriction
{
    boolean isRestricted(Video video);
}
