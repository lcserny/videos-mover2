package net.cserny.videosmover.service;

import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoPath;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.model.VideoType;

import java.util.List;
import java.util.Map;

public interface CachedTmdbService {

    String MOVIE_PREFIX = "MOVIE_";
    String TVSHOW_PREFIX = "TVSHOW_";
    String DEFAULT_POSTER_WIDTH = "w92";
    String POSTER_URL_PATTERN = "http://image.tmdb.org/t/p/" + DEFAULT_POSTER_WIDTH + "%s";
    int DEFAULT_CAST_SIZE = 5;
    int DEFAULT_VIDEOS_SIZE = 5;

    List<VideoMetadata> searchMovieMetadata(VideoQuery movieQuery) throws Exception;

    List<VideoMetadata> searchTvShowMetadata(VideoQuery tvShowQuery) throws Exception;

    Map<String, List<VideoMetadata>> getVideoCache();

    VideoPath adjustVideoPath(VideoPath videoPath, VideoType videoType) throws Exception;

    String keyFormat(String prefix, VideoQuery query);
}
