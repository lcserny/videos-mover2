package net.cserny.videosmover.core.service.parser;

import net.cserny.videosmover.core.model.Video;
import net.cserny.videosmover.core.service.observer.VideoAdjustmentObserver;

import java.util.List;

public interface VideoNameParser {

    void parseTvShow(Video video, List<VideoAdjustmentObserver> observers);

    void parseMovie(Video video, List<VideoAdjustmentObserver> observers);
}
