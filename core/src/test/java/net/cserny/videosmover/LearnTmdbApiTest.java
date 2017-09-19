package net.cserny.videosmover;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import org.junit.Test;

public class LearnTmdbApiTest {
    @Test
    public void querySomething() throws Exception {
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
}
