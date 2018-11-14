package net.cserny.videosmover.service;

import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.tv.TvSeries;
import net.cserny.videosmover.helper.PropertiesLoader;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.model.VideoType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.function.Supplier;

// TODO: change this to something faster?
@Singleton
public class CachedTmdbService implements CachedMetadataService {

    private static final String POSTER_URL_PATTERN = "http://image.tmdb.org/t/p/w92%s";
    private static final String NO_API_KEY = "<CHANGE_ME>";

    private final SimpleMessageRegistry messageRegistry;

    private Map<String, List<VideoMetadata>> videoCache = new HashMap<>(50);
    private TmdbApi tmdbApi;

    @Inject
    public CachedTmdbService(SimpleMessageRegistry messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    private void initApi() {
        String apiKey = PropertiesLoader.getTmdbApiKey();
        if (apiKey.equals(NO_API_KEY)) {
            messageRegistry.displayMessage(MessageProvider.noMetadataServiceApiKey());
        }
        this.tmdbApi = new TmdbApi(apiKey);
    }

    private TmdbApi getTmdbApi() {
        if (tmdbApi == null) {
            initApi();
        }
        return tmdbApi;
    }

    @Override
    public List<VideoMetadata> searchMetadata(VideoQuery query, VideoType type) {
        switch (type) {
            case MOVIE:
                return searchMovieMetadata(query);
            case TVSHOW:
                return searchTvShowMetadata(query);
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public Map<String, List<VideoMetadata>> getVideoCache() {
        return videoCache;
    }

    @Override
    public void adjustVideoPath(Video video) {
        VideoQuery query = VideoQuery.newInstance()
                .withName(video.getOutputFolderName())
                .withYear(video.getDate().getYear())
                .build();

        List<VideoMetadata> metadataList = Collections.emptyList();
        // TODO: make OOP, use a class sent in constructor that knows how to handle
        switch (video.getVideoType()) {
            case MOVIE:
                metadataList = searchMovieMetadata(query);
                break;
            case TVSHOW:
                metadataList = searchTvShowMetadata(query);
                break;
        }

        if (!metadataList.isEmpty()) {
            VideoMetadata metadata = metadataList.get(0);
            video.setOutputFolderName(metadata.getName());
            video.getDate().setFromReleaseDate(metadata.getReleaseDate());
        }
    }

    @Override
    public String keyFormat(String prefix, VideoQuery query) {
        String formatted = prefix + query.getName();
        formatted = query.getYear() != null ? formatted + "_" + query.getYear() : formatted;
        formatted = formatted.replaceAll(" ", "_");
        return formatted.toLowerCase();
    }

    private List<VideoMetadata> searchMovieMetadata(VideoQuery movieQuery) {
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

    private List<VideoMetadata> searchTvShowMetadata(VideoQuery tvShowQuery) {
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

    private List<VideoMetadata> searchInternal(VideoQuery query, String keyPrefix,
                                               Supplier<List<VideoMetadata>> callback) {
        if (emptyQuery(query)) {
            return Collections.emptyList();
        }

        String cacheKey = keyFormat(keyPrefix, query);
        if (videoCache.containsKey(cacheKey)) {
            return videoCache.get(cacheKey);
        }

        List<VideoMetadata> metadataList = callback.get();
        videoCache.put(cacheKey, metadataList);

        return metadataList;
    }

    private String buildPosterUrl(String posterPath) {
        if (posterPath == null || posterPath.isEmpty()) {
            try {
                URL resource = getClass().getResource(
                        StaticPathsProvider.getJoinedPathString("/images", "no-poster.jpg"));
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
