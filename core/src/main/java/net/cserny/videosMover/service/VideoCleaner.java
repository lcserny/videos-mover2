package net.cserny.videosMover.service;


import net.cserny.videosMover.model.Video;

import java.io.IOException;
import java.util.List;

public interface VideoCleaner
{
    void clean(Video video) throws IOException;

    void cleanAll(List<Video> videos) throws IOException;
}
