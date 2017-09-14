package net.cserny.videosMover.service;


import net.cserny.videosMover.model.Video;

import java.io.IOException;
import java.util.List;

/**
 * Created by leonardo on 02.09.2017.
 */
public interface ScanService
{
    List<Video> scan(String location) throws IOException;
}
