package net.cserny.videosmover.core.helper;

import net.cserny.videosmover.core.model.Video;
import net.cserny.videosmover.core.service.observer.VideoAdjustmentObserver;

import java.util.List;

@FunctionalInterface
public interface ResolvingFunction {
    void process(Video video, List<VideoAdjustmentObserver> observers);
}
