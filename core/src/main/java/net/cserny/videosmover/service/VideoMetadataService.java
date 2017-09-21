package net.cserny.videosmover.service;

import net.cserny.videosmover.model.VideoMetadata;

import java.util.List;

public interface VideoMetadataService {
    List<VideoMetadata> searchMovieMetadata(String movieQuery);

    List<VideoMetadata> searchTvShowMetadata(String tvShowQuery);

    List<String> getMovieCast(int movieId);

    List<String> getTvShowCast(int tvShowId);
}
