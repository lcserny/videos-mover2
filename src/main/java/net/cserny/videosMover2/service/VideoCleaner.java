package net.cserny.videosMover2.service;

import net.cserny.videosMover2.dto.Video;

import java.io.IOException;
import java.util.List;

public interface VideoCleaner
{
    void clean(Video video) throws IOException;

    void cleanAll(List<Video> videos) throws IOException;
}
