package net.cserny.videosmover.core.service.parser;

import net.cserny.videosmover.core.model.Video;
import net.cserny.videosmover.core.model.VideoType;

public interface OutputVideoNameChecker {
    boolean canHandle(VideoType type);
    void check(Video video);
}
