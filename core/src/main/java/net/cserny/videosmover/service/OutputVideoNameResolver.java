package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoType;

public interface OutputVideoNameResolver {
    boolean canHandle(VideoType type);

    String resolve(Video video);
}
