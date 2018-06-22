package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.model.VideoPath;
import net.cserny.videosmover.service.observer.VideoAdjustmentObserver;

import java.util.List;

public interface VideoNameParser {
    void parseTvShow(VideoPath text, List<VideoAdjustmentObserver> observers);

    void parseMovie(VideoPath text, List<VideoAdjustmentObserver> observers);
}
