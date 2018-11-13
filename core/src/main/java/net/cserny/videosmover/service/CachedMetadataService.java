package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.model.VideoType;

import java.util.List;
import java.util.Map;

public interface CachedMetadataService {

    String MOVIE_PREFIX = "MOVIE_";
    String TVSHOW_PREFIX = "TVSHOW_";
    int DEFAULT_CAST_SIZE = 5;
    int DEFAULT_VIDEOS_SIZE = 5;

    List<VideoMetadata> searchMetadata(VideoQuery query, VideoType type);

    Map<String, List<VideoMetadata>> getVideoCache();

    void adjustVideoPath(Video video);

    String keyFormat(String prefix, VideoQuery query);
}
