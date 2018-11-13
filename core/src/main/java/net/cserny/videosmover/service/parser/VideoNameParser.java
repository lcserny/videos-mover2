package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.observer.VideoAdjustmentObserver;

import java.util.List;

public interface VideoNameParser {

    void parseTvShow(Video video, List<VideoAdjustmentObserver> observers);

    void parseMovie(Video video, List<VideoAdjustmentObserver> observers);
}
