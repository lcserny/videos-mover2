package net.cserny.videosMover2.service;

import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.dto.VideoRow;

import java.util.List;

/**
 * Created by leonardo on 03.09.2017.
 */
public class VideoMoverImpl implements VideoMover
{
    @Override
    public boolean move(Video video) {
        return false;
    }

    @Override
    public boolean moveAll(List<VideoRow> videoRowList) {
        return false;
    }
}
