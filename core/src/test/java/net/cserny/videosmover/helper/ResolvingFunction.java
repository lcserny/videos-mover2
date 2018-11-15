package net.cserny.videosmover.helper;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.observer.VideoAdjustmentObserver;

import java.util.List;

@FunctionalInterface
public interface ResolvingFunction {
    void process(Video video, List<VideoAdjustmentObserver> observers);
}
