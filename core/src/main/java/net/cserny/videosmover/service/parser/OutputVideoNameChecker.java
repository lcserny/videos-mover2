package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoType;

public interface OutputVideoNameChecker {
    boolean canHandle(VideoType type);
    void check(Video video);
}
