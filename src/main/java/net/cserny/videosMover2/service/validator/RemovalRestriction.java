package net.cserny.videosMover2.service.validator;

import net.cserny.videosMover2.dto.Video;

/**
 * Created by leonardo on 12.09.2017.
 */
public interface RemovalRestriction
{
    boolean isRestricted(Video video);
}
