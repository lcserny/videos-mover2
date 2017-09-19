package net.cserny.videosmover.service;


import net.cserny.videosmover.model.Video;

import java.util.List;

public interface VideoCleaner {
    void clean(Video video);

    void cleanAll(List<Video> videos);
}
