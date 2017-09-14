package net.cserny.videosMover.service;


import net.cserny.videosMover.model.Video;

/**
 * Created by leonardo on 02.09.2017.
 */
public interface OutputNameResolver
{
    String resolve(Video video);
}
