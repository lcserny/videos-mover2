package net.cserny.videosmover.service;


import net.cserny.videosmover.model.Video;

import java.io.IOException;
import java.util.List;

/**
 * Created by leonardo on 03.09.2017.
 */
public interface VideoMover {
    boolean move(Video video) throws IOException;

    boolean moveAll(List<Video> videoList) throws IOException;
}
