package net.cserny.videosMover2.service;

import net.cserny.videosMover2.dto.Video;

import java.io.IOException;
import java.util.List;

/**
 * Created by leonardo on 02.09.2017.
 */
public interface ScanService
{
    List<Video> scan(String location) throws IOException;
}
