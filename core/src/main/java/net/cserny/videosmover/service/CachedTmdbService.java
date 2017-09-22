package net.cserny.videosmover.service;

import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.tv.TvSeries;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class CachedTmdbService implements VideoMetadataService {
    public static final String MOVIE_PREFIX = "MOVIE_";
    public static final String TVSHOW_PREFIX = "TVSHOW_";
    public static final String DEFAULT_POSTER_WIDTH = "w185";
    public static final String POSTER_URL_PATTERN = "http://image.tmdb.org/t/p/" + DEFAULT_POSTER_WIDTH + "%s";
    public static final int DEFAULT_CAST_SIZE = 5;
    public static final int DEFAULT_VIDEOS_SIZE = 5;

    private Map<String, List<VideoMetadata>> videoCache = Collections.synchronizedMap(new HashMap<>(50));
    private TmdbApi tmdbApi;

    @Value("${tmdb.api.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        this.tmdbApi = new TmdbApi(apiKey);
    }

    @Override
    public List<VideoMetadata> searchMovieMetadata(VideoQuery movieQuery) {
        return searchInternal(movieQuery, MOVIE_PREFIX, metadataList -> {
            MovieResultsPage results = searchMovieInternal(movieQuery);
            int maxIndex = Math.min(DEFAULT_VIDEOS_SIZE, results.getResults().size());
            for (int i = 0; i < maxIndex; i++) {
                MovieDb movieResult = results.getResults().get(i);
                VideoMetadata metadata = new VideoMetadata();
                metadata.setName(movieResult.getTitle());
                metadata.setReleaseDate(movieResult.getReleaseDate());
                metadata.setPosterUrl(buildPosterUrl(movieResult.getPosterPath()));
                metadata.setDescription(movieResult.getOverview());
                metadata.setCast(getMovieCast(movieResult.getId()));
                metadataList.add(metadata);
            }
        });
    }

    @Override
    public List<VideoMetadata> searchTvShowMetadata(VideoQuery tvShowQuery) {
        return searchInternal(tvShowQuery, TVSHOW_PREFIX, metadataList -> {
            TvResultsPage results = searchTvInternal(tvShowQuery);
            int maxIndex = Math.min(DEFAULT_VIDEOS_SIZE, results.getResults().size());
            for (int i = 0; i < maxIndex; i++) {
                TvSeries tvResult = results.getResults().get(i);
                VideoMetadata metadata = new VideoMetadata();
                metadata.setName(tvResult.getName());
                metadata.setReleaseDate(tvResult.getFirstAirDate());
                metadata.setPosterUrl(buildPosterUrl(tvResult.getPosterPath()));
                metadata.setDescription(tvResult.getOverview());
                metadata.setCast(getTvShowCast(tvResult.getId()));
                metadataList.add(metadata);
            }
        });
    }

    private List<VideoMetadata> searchInternal(VideoQuery query, String keyPrefix, MetadataSearchCallback callback) {
        List<VideoMetadata> metadataList = new ArrayList<>();
        if (emptyQuery(query)) {
            return metadataList;
        }

        String cacheKey = keyFormat(keyPrefix, query);
        if (videoCache.containsKey(cacheKey)) {
            return videoCache.get(cacheKey);
        }

        callback.populateMetadata(metadataList);
        videoCache.put(cacheKey, metadataList);

        return metadataList;
    }

    private String buildPosterUrl(String posterPath) {
        return String.format(POSTER_URL_PATTERN, posterPath);
    }

    private boolean emptyQuery(VideoQuery query) {
        return query == null || query.getName().isEmpty();
    }

    private MovieResultsPage searchMovieInternal(VideoQuery query) {
        TmdbSearch search = tmdbApi.getSearch();
        return query.getYear() != null
                ? query.getLanguage() != null
                        ? search.searchMovie(query.getName(), query.getYear(), query.getLanguage(), false, 1)
                        : search.searchMovie(query.getName(), query.getYear(), null, false, 1)
                : search.searchMovie(query.getName(), null, null, false, 1);
    }

    private String keyFormat(String prefix, VideoQuery query) {
        String formatted = prefix + query.getName();
        formatted = query.getYear() != null ? formatted + "_" + query.getYear() : formatted;
        formatted = formatted.replaceAll(" ", "_");
        return formatted.toLowerCase();
    }

    private TvResultsPage searchTvInternal(VideoQuery query) {
        TmdbSearch search = tmdbApi.getSearch();
        return query.getLanguage() != null
                ? search.searchTv(query.getName(), query.getLanguage(), 1)
                : search.searchTv(query.getName(), null, 1);
    }

    private List<String> getMovieCast(int movieId) {
        MovieDb movie = tmdbApi.getMovies().getMovie(movieId, null, TmdbMovies.MovieMethod.credits);
        return searchForCast(movieId, movie.getCredits());
    }

    private List<String> getTvShowCast(int tvShowId) {
        TvSeries series = tmdbApi.getTvSeries().getSeries(tvShowId, null, TmdbTV.TvMethod.credits);
        return searchForCast(tvShowId, series.getCredits());
    }

    private List<String> searchForCast(int id, Credits credits) {
        List<String> cast = new ArrayList<>();
        List<PersonCast> personCastList = credits != null ? credits.getCast() : new ArrayList<>();
        int maxIndex = Math.min(DEFAULT_CAST_SIZE, personCastList.size());
        for (int i = 0; i < maxIndex; i++) {
            PersonCast person = personCastList.get(i);
            cast.add(person.getName());
        }
        return cast;
    }

    private interface MetadataSearchCallback {
        void populateMetadata(List<VideoMetadata> metadataList);
    }
}
