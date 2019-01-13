package net.cserny.videosmover.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.*;
import com.uwetrottmann.tmdb2.enumerations.AppendToResponseItem;
import com.uwetrottmann.tmdb2.services.SearchService;
import net.cserny.videosmover.helper.PreferencesLoader;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.model.VideoType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Singleton
public class TrottmannCachedTmdbService implements CachedMetadataService {

    private static final String POSTER_URL_PATTERN = "http://image.tmdb.org/t/p/w92%s";

    private final SimpleMessageRegistry messageRegistry;
    private Tmdb tmdbApi;

    @Inject
    public TrottmannCachedTmdbService(SimpleMessageRegistry messageRegistry) {
        this.messageRegistry = messageRegistry;
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

    private List<VideoMetadata> searchMovieMetadata(VideoQuery movieQuery) {
        return searchInternal(movieQuery, MOVIE_PREFIX, () -> {
            MovieResultsPage resultsPage = searchMovieInternal(movieQuery);
            if (resultsPage != null) {
                List<BaseMovie> movies = resultsPage.results;
                int maxIndex = Math.min(DEFAULT_VIDEOS_SIZE, movies.size());
                List<VideoMetadata> metadataList = new ArrayList<>();
                for (int i = 0; i < maxIndex; i++) {
                    VideoMetadata metadata = buildMovieVideoMetadata(movies.get(i));
                    metadataList.add(metadata);
                }
                return metadataList;
            }
            return null;
        });
    }

    private VideoMetadata buildMovieVideoMetadata(BaseMovie movieResult) {
        VideoMetadata metadata = new VideoMetadata();
        metadata.setName(movieResult.title);
        metadata.setReleaseDate(movieResult.release_date);
        metadata.setPosterUrl(buildPosterUrl(movieResult.poster_path));
        metadata.setDescription(movieResult.overview);
        metadata.setCast(getMovieCast(movieResult.id));
        return metadata;
    }

    private List<VideoMetadata> searchTvShowMetadata(VideoQuery tvShowQuery) {
        return searchInternal(tvShowQuery, TVSHOW_PREFIX, () -> {
            TvShowResultsPage tvShowResultsPage = searchTvInternal(tvShowQuery);
            if (tvShowResultsPage != null) {
                List<BaseTvShow> tvShows = tvShowResultsPage.results;
                int maxIndex = Math.min(DEFAULT_VIDEOS_SIZE, tvShows.size());
                List<VideoMetadata> metadataList = new ArrayList<>();
                for (int i = 0; i < maxIndex; i++) {
                    VideoMetadata metadata = buildTvShowVideoMetadata(tvShows.get(i));
                    metadataList.add(metadata);
                }
                return metadataList;
            }
            return null;
        });
    }

    private VideoMetadata buildTvShowVideoMetadata(BaseTvShow tvResult) {
        VideoMetadata metadata = new VideoMetadata();
        metadata.setName(tvResult.name);
        metadata.setReleaseDate(tvResult.first_air_date);
        metadata.setPosterUrl(buildPosterUrl(tvResult.poster_path));
        metadata.setDescription(tvResult.overview);
        metadata.setCast(getTvShowCast(tvResult.id));
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
        SearchService search = getTmdbApi().searchService();
        try {
            return search.movie(query.getName(), 1, query.getLanguage(), false, query.getYear(),
                    null, null)
                    .execute().body();
        } catch (IOException e) {
            messageRegistry.displayMessage(MessageProvider.problemOccurred());
        }
        return null;
    }

    private TvShowResultsPage searchTvInternal(VideoQuery query) {
        SearchService search = getTmdbApi().searchService();
        try {
            return search.tv(query.getName(), 1, query.getLanguage(), null, null)
                    .execute().body();
        } catch (IOException e) {
            messageRegistry.displayMessage(MessageProvider.problemOccurred());
        }
        return null;
    }

    private List<String> getMovieCast(int movieId) {
        try {
            return searchForCast(movieId, getTmdbApi().moviesService().credits(movieId).execute().body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private List<String> getTvShowCast(int tvShowId) {
        try {
            return searchForCast(tvShowId, getTmdbApi().tvService().credits(tvShowId, null).execute().body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private List<String> searchForCast(int id, Credits credits) {
        if (credits == null) {
            return Collections.emptyList();
        }

        List<CastMember> personCastList = credits.cast;
        int maxIndex = Math.min(DEFAULT_CAST_SIZE, personCastList.size());
        List<String> cast = new ArrayList<>();
        for (int i = 0; i < maxIndex; i++) {
            CastMember person = personCastList.get(i);
            cast.add(person.name);
        }
        return cast;
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

    public Tmdb getTmdbApi() {
        if (tmdbApi == null || apiKeyChanged.get()) {
            initApi();
        }
        return tmdbApi;
    }

    private void initApi() {
        String apiKey = PreferencesLoader.getOnlineMetadataApiKey();
        if (apiKey.equals(NO_API_KEY)) {
            messageRegistry.displayMessage(MessageProvider.noMetadataServiceApiKey());
        }
        apiKeyChanged.set(false);
        this.tmdbApi = new Tmdb(apiKey);
    }
}
