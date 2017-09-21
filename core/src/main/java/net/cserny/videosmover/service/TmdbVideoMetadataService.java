package net.cserny.videosmover.service;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.TvResultsPage;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.tv.TvSeries;
import net.cserny.videosmover.model.VideoMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

// TODO: refactor duplicate code + show the poster somehow?
@Service
public class TmdbVideoMetadataService implements VideoMetadataService {
    private TmdbApi tmdbApi;
    private String defaultPosterSize = "w185";
    private String imageUrlPattern = "http://image.tmdb.org/t/p/" + defaultPosterSize + "%s";

    @Value("${tmdb.api.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        this.tmdbApi = new TmdbApi(apiKey);
    }

    @Override
    public List<VideoMetadata> searchMovieMetadata(String movieQuery) {
        List<VideoMetadata> metadataList = new ArrayList<>();
        if (movieQuery.isEmpty()) {
            return metadataList;
        }

        MovieResultsPage results = tmdbApi.getSearch().searchMovie(movieQuery, null, null, false, 1);
        int maxIndex = Math.min(5, results.getTotalResults());
        for (int i = 0; i < maxIndex; i++) {
            MovieDb movieResult = results.getResults().get(i);
            VideoMetadata metadata = new VideoMetadata();
            metadata.setName(movieResult.getTitle());
            metadata.setReleaseDate(movieResult.getReleaseDate());
            metadata.setPosterUrl(String.format(imageUrlPattern, movieResult.getPosterPath()));
            metadata.setDescription(movieResult.getOverview());
            metadata.setCast(getMovieCast(movieResult.getId()));
            metadataList.add(metadata);
        }

        return metadataList;
    }

    @Override
    public List<VideoMetadata> searchTvShowMetadata(String tvShowQuery) {
        List<VideoMetadata> metadataList = new ArrayList<>();
        if (tvShowQuery.isEmpty()) {
            return metadataList;
        }

        TvResultsPage results = tmdbApi.getSearch().searchTv(tvShowQuery, null, 1);
        int maxIndex = Math.min(5, results.getTotalResults());
        for (int i = 0; i < maxIndex; i++) {
            TvSeries tvResult = results.getResults().get(i);
            VideoMetadata metadata = new VideoMetadata();
            metadata.setName(tvResult.getName());
            metadata.setReleaseDate(tvResult.getFirstAirDate());
            metadata.setPosterUrl(String.format(imageUrlPattern, tvResult.getPosterPath()));
            metadata.setDescription(tvResult.getOverview());
            metadata.setCast(getTvShowCast(tvResult.getId()));
            metadataList.add(metadata);
        }

        return metadataList;
    }

    @Override
    public List<String> getMovieCast(int movieId) {
        List<String> cast = new ArrayList<>();
        MovieDb movie = tmdbApi.getMovies().getMovie(movieId, null, TmdbMovies.MovieMethod.credits);
        int maxIndex = Math.min(5, movie.getCredits().getCast().size());
        for (int i = 0; i < maxIndex; i++) {
            PersonCast person = movie.getCredits().getCast().get(i);
            cast.add(person.getName());
        }
        return cast;
    }

    @Override
    public List<String> getTvShowCast(int tvShowId) {
        List<String> cast = new ArrayList<>();
        TvSeries series = tmdbApi.getTvSeries().getSeries(tvShowId, null, TmdbTV.TvMethod.credits);
        int maxIndex = Math.min(5, series.getCredits().getCast().size());
        for (int i = 0; i < maxIndex; i++) {
            PersonCast person = series.getCredits().getCast().get(i);
            cast.add(person.getName());
        }
        return cast;
    }
}
