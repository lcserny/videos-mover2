package net.cserny.videosmover.learning;

import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.tv.TvSeries;
import org.junit.Test;

public class LearnTmdbApiTest {
    @Test
    public void queryMovie() throws Exception {
        TmdbApi api = new TmdbApi("c37791bac5bebdfeb3e73db3632b9a13");
        TmdbSearch search = api.getSearch();
        MovieResultsPage movie = search.searchMovie("Fight Club", null, "en", false, null);
        for (MovieDb movieInfo : movie) {
            System.out.println("title: " + movieInfo.getTitle());
            System.out.println("releaseDate: " + movieInfo.getReleaseDate());
            System.out.println("cast: " + movieInfo.getCast());
            System.out.println("crew: " + movieInfo.getCrew());
            System.out.println("genres: " + movieInfo.getGenres());
            System.out.println("originalLangs: " + movieInfo.getOriginalLanguage());
            System.out.println("overview: " + movieInfo.getOverview());
            System.out.println("images: " + movieInfo.getImages());
            System.out.println("mediaType: " + movieInfo.getMediaType());
            String size = "w185";
            System.out.println("posterPath: " + String.format("http://image.tmdb.org/t/p/%s/%s", size, movieInfo.getPosterPath()));
            System.out.println("runtime: " + movieInfo.getRuntime());
            System.out.println("status: " + movieInfo.getStatus());
            System.out.println("userRating: " + movieInfo.getUserRating());
            System.out.println("videos: " + movieInfo.getVideos());
            System.out.println("id: " + movieInfo.getId());
            System.out.println();
        }
    }

    @Test
    public void queryTvShow() throws Exception {
        TmdbApi api = new TmdbApi("c37791bac5bebdfeb3e73db3632b9a13");
        TmdbSearch search = api.getSearch();
        TvResultsPage tvSeries = search.searchTv("game of thrones", null, null);
        for (TvSeries series : tvSeries) {
            System.out.println("name: " + series.getOriginalName());
            System.out.println("firstAirDate: " + series.getFirstAirDate());
            System.out.println("genres: " + series.getGenres());
            System.out.println("credits: " + series.getCredits());
            System.out.println("overview: " + series.getOverview());
            System.out.println("images: " + series.getImages());
            System.out.println("mediaType: " + series.getMediaType());
            String size = "w185";
            System.out.println("posterPath: " + String.format("http://image.tmdb.org/t/p/%s/%s", size, series.getPosterPath()));
            System.out.println("status: " + series.getStatus());
            System.out.println("userRating: " + series.getUserRating());
            System.out.println("videos: " + series.getVideos());
            System.out.println("id: " + series.getId());
            System.out.println();
        }
    }

    @Test
    public void findMovieById() throws Exception {
        TmdbApi api = new TmdbApi("c37791bac5bebdfeb3e73db3632b9a13");
        MovieDb movie = api.getMovies().getMovie(550, null);
        System.out.println("title: " + movie.getTitle());
        System.out.println("releaseDate: " + movie.getReleaseDate());
        System.out.println("cast: " + movie.getCast());
        System.out.println("crew: " + movie.getCrew());
        System.out.println("genres: " + movie.getGenres());
        System.out.println("originalLangs: " + movie.getOriginalLanguage());
        System.out.println("overview: " + movie.getOverview());
        System.out.println("images: " + movie.getImages());
        System.out.println("mediaType: " + movie.getMediaType());
        String size = "w185";
        System.out.println("posterPath: " + String.format("http://image.tmdb.org/t/p/%s/%s", size, movie.getPosterPath()));
        System.out.println("runtime: " + movie.getRuntime());
        System.out.println("status: " + movie.getStatus());
        System.out.println("userRating: " + movie.getUserRating());
        System.out.println("videos: " + movie.getVideos());
        System.out.println("id: " + movie.getId());
    }

    @Test
    public void findCastByMovie() throws Exception {
        TmdbApi api = new TmdbApi("c37791bac5bebdfeb3e73db3632b9a13");
        MovieDb movie = api.getMovies().getMovie(550, null, TmdbMovies.MovieMethod.credits);
        for (PersonCast person : movie.getCast()) {
            System.out.println(person.getName());
        }
    }

    @Test
    public void findCastByTvShow() throws Exception {
        TmdbApi api = new TmdbApi("c37791bac5bebdfeb3e73db3632b9a13");
        TvSeries series = api.getTvSeries().getSeries(1399, null, TmdbTV.TvMethod.credits);
        for (PersonCast person : series.getCredits().getCast()) {
            System.out.println(person.getName());
        }
    }
}
