package net.cserny.videosmover.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.tv.TvSeries;
import net.cserny.videosmover.helper.PreferencesLoader;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.model.VideoType;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

// TODO: change this to something faster?
// TODO: improve the isEnabled in all methods
@Singleton
public class MovitoCachedTmdbService implements CachedMetadataService {

    private static final String POSTER_URL_PATTERN = "http://image.tmdb.org/t/p/w92%s";

    private final SimpleMessageRegistry messageRegistry;
    private TmdbApi tmdbApi;

    @Inject
    public MovitoCachedTmdbService(SimpleMessageRegistry messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    private void initApi() {
        String apiKey = PreferencesLoader.getOnlineMetadataApiKey();
        if (apiKey.equals(NO_API_KEY)) {
            messageRegistry.displayMessage(MessageProvider.noMetadataServiceApiKey());
        }
        apiKeyChanged.set(false);
        this.tmdbApi = new TmdbApi(apiKey);
    }

    private TmdbApi getTmdbApi() {
        if (tmdbApi == null || apiKeyChanged.get()) {
            initApi();
        }
        return tmdbApi;
    }

    @Override
    public List<VideoMetadata> searchMetadata(VideoQuery query, VideoType type) {
        if (!isEnabled()) {
            return Collections.emptyList();
        }

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
    public void adjustOutputAndDate(Video video) {
        if (!isEnabled()) {
            return;
        }

        VideoQuery query = VideoQuery.newInstance()
                .withName(video.getOutputFolderWithoutDate())
                .withYear(video.getYear())
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
            video.setOutputFolderWithoutDate(metadata.getName());
            video.setDateFromReleaseDate(metadata.getReleaseDate());
        }
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
