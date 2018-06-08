package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;

public abstract class AbstractOutputVideoNameResolver implements OutputVideoNameResolver {

    protected String getExtension(Video video) {
        return video.getInputFilename().substring(video.getInputFilename().lastIndexOf('.'));
    }
}
