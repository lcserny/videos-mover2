package net.cserny.videosmover.service;

import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.tv.TvSeries;
import net.cserny.videosmover.helper.PropertiesLoader;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;

@Singleton
public class CachedTmdbService {

    public static final String MOVIE_PREFIX = "MOVIE_";
    public static final String TVSHOW_PREFIX = "TVSHOW_";
    public static final String DEFAULT_POSTER_WIDTH = "w92";
    public static final String POSTER_URL_PATTERN = "http://image.tmdb.org/t/p/" + DEFAULT_POSTER_WIDTH + "%s";
    public static final int DEFAULT_CAST_SIZE = 5;
    public static final int DEFAULT_VIDEOS_SIZE = 5;

    private Map<String, List<VideoMetadata>> videoCache = new HashMap<>(50);
    private TmdbApi tmdbApi;

    @Inject
    public CachedTmdbService() { }

    private void initApi() {
        this.tmdbApi = new TmdbApi(PropertiesLoader.getTmdbApiKey());
    }

    private TmdbApi getTmdbApi() {
        if (tmdbApi == null) {
            initApi();
        }
        return tmdbApi;
    }

    public List<VideoMetadata> searchMovieMetadata(VideoQuery movieQuery) throws Exception {
        return searchInternal(movieQuery, MOVIE_PREFIX, () -> {
            List<MovieDb> results = searchMovieInternal(movieQuery).getResults();
            int maxIndex = Math.min(DEFAULT_VIDEOS_SIZE, results.size());
            List<VideoMetadata> metadataList = new ArrayList<>();
            for (int i = 0; i < maxIndex; i++) {
                VideoMetadata metadata = buildMovieVideoMetadata(results.get(i));
                metadataList.add(metadata);
            }
            return metadataList;
        });
    }

    private VideoMetadata buildMovieVideoMetadata(MovieDb movieResult) {
        VideoMetadata metadata = new VideoMetadata();
        metadata.setName(movieResult.getTitle());
        metadata.setReleaseDate(movieResult.getReleaseDate());
        metadata.setPosterUrl(buildPosterUrl(movieResult.getPosterPath()));
        metadata.setDescription(movieResult.getOverview());
        metadata.setCast(getMovieCast(movieResult.getId()));
        return metadata;
    }

    public List<VideoMetadata> searchTvShowMetadata(VideoQuery tvShowQuery) throws Exception {
        return searchInternal(tvShowQuery, TVSHOW_PREFIX, () -> {
            List<TvSeries> results = searchTvInternal(tvShowQuery).getResults();
            int maxIndex = Math.min(DEFAULT_VIDEOS_SIZE, results.size());
            List<VideoMetadata> metadataList = new ArrayList<>();
            for (int i = 0; i < maxIndex; i++) {
                VideoMetadata metadata = buildTvShowVideoMetadata(results.get(i));
                metadataList.add(metadata);
            }
            return metadataList;
        });
    }

    private VideoMetadata buildTvShowVideoMetadata(TvSeries tvResult) {
        VideoMetadata metadata = new VideoMetadata();
        metadata.setName(tvResult.getName());
        metadata.setReleaseDate(tvResult.getFirstAirDate());
        metadata.setPosterUrl(buildPosterUrl(tvResult.getPosterPath()));
        metadata.setDescription(tvResult.getOverview());
        metadata.setCast(getTvShowCast(tvResult.getId()));
        return metadata;
    }

    public Map<String, List<VideoMetadata>> getVideoCache() {
        return videoCache;
    }

    private List<VideoMetadata> searchInternal(VideoQuery query, String keyPrefix, Callable<List<VideoMetadata>> callback) throws Exception {
        if (emptyQuery(query)) {
            return Collections.emptyList();
        }

        String cacheKey = keyFormat(keyPrefix, query);
        if (videoCache.containsKey(cacheKey)) {
            return videoCache.get(cacheKey);
        }

        List<VideoMetadata> metadataList = callback.call();
        videoCache.put(cacheKey, metadataList);

        return metadataList;
    }

    private String buildPosterUrl(String posterPath) {
        if (posterPath == null || posterPath.isEmpty()) {
            try {
                URL resource = getClass().getResource("/images/no-poster.jpg");
                if (resource != null) {
                  return resource.toURI().toString();
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return String.format(POSTER_URL_PATTERN, posterPath);
    }

    private boolean emptyQuery(VideoQuery query) {
        return query == null || (query.getName() == null || query.getName().isEmpty());
    }

    private MovieResultsPage searchMovieInternal(VideoQuery query) {
        TmdbSearch search = getTmdbApi().getSearch();
        return query.getYear() != null
                ? query.getLanguage() != null
                        ? search.searchMovie(query.getName(), query.getYear(), query.getLanguage(), false, 1)
                        : search.searchMovie(query.getName(), query.getYear(), null, false, 1)
                : search.searchMovie(query.getName(), null, null, false, 1);
    }

    public String keyFormat(String prefix, VideoQuery query) {
        String formatted = prefix + query.getName();
        formatted = query.getYear() != null ? formatted + "_" + query.getYear() : formatted;
        formatted = formatted.replaceAll(" ", "_");
        return formatted.toLowerCase();
    }

    private TvResultsPage searchTvInternal(VideoQuery query) {
        TmdbSearch search = getTmdbApi().getSearch();
        return query.getLanguage() != null
                ? search.searchTv(query.getName(), query.getLanguage(), 1)
                : search.searchTv(query.getName(), null, 1);
    }

    private List<String> getMovieCast(int movieId) {
        MovieDb movie = getTmdbApi().getMovies().getMovie(movieId, null, TmdbMovies.MovieMethod.credits);
        return searchForCast(movieId, movie.getCredits());
    }

    private List<String> getTvShowCast(int tvShowId) {
        TvSeries series = getTmdbApi().getTvSeries().getSeries(tvShowId, null, TmdbTV.TvMethod.credits);
        return searchForCast(tvShowId, series.getCredits());
    }

    private List<String> searchForCast(int id, Credits credits) {
        if (credits == null) {
            return Collections.emptyList();
        }

        List<PersonCast> personCastList = credits.getCast();
        int maxIndex = Math.min(DEFAULT_CAST_SIZE, personCastList.size());
        List<String> cast = new ArrayList<>();
        for (int i = 0; i < maxIndex; i++) {
            PersonCast person = personCastList.get(i);
            cast.add(person.getName());
        }
        return cast;
    }
}
