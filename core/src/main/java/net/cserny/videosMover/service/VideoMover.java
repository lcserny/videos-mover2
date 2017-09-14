package net.cserny.videosMover.service;


import net.cserny.videosMover.model.Video;

import java.io.IOException;
import java.util.List;

/**
 * Created by leonardo on 03.09.2017.
 */
public interface VideoMover
{
    boolean move(Video video) throws IOException;

    boolean moveAll(List<Video> videoList) throws IOException;
}
