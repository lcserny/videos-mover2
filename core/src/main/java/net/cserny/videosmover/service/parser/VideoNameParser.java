package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.model.VideoPath;

public interface VideoNameParser {
    void parseTvShow(VideoPath text);

    void parseMovie(VideoPath text);
}
