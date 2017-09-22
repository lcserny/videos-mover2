package net.cserny.videosmover.service;

import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;

import java.util.List;

public interface VideoMetadataService {
    List<VideoMetadata> searchMovieMetadata(VideoQuery movieQuery);

    List<VideoMetadata> searchTvShowMetadata(VideoQuery tvShowQuery);
}
