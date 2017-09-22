package net.cserny.videosmover.service;


import net.cserny.videosmover.model.SimpleVideoOutput;
import net.cserny.videosmover.model.Video;

/**
 * Created by leonardo on 02.09.2017.
 */
public interface OutputResolver {
    SimpleVideoOutput resolve(Video video);
}
