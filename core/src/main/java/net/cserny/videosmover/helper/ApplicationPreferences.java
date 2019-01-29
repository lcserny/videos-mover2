package net.cserny.videosmover.helper;

import net.cserny.videosmover.service.CachedMetadataService;

import java.util.prefs.Preferences;

import static net.cserny.videosmover.constants.PropertyConstants.*;

public class ApplicationPreferences {

    private static final Preferences preferences = Preferences.userNodeForPackage(ApplicationPreferences.class);

    private static final String DEFAULT_DOWNLOADS = "/Downloads";
    private static final String DEFAULT_MOVIES = "/Movies";
    private static final String DEFAULT_TV = "/TvShows";
    private static final String DEFAULT_APIKEY = CachedMetadataService.NO_API_KEY;
    private static final boolean DEFAULT_ENABLE_METADATA_SEARCH = false;

    private ApplicationPreferences() {
    }

    public static void setDownloadsPath(String downloadsPath) {
        preferences.put(PATH_DOWNLOADS_KEY, downloadsPath);
    }

    public static String getDownloadsPath() {
        return preferences.get(PATH_DOWNLOADS_KEY, DEFAULT_DOWNLOADS);
    }

    public static void setMoviesPath(String moviesPath) {
        preferences.put(PATH_MOVIES_KEY, moviesPath);
    }

    public static String getMoviesPath() {
        return preferences.get(PATH_MOVIES_KEY, DEFAULT_MOVIES);
    }

    public static void setTvShowsPath(String tvShowsPath) {
        preferences.put(PATH_TVSHOWS_KEY, tvShowsPath);
    }

    public static String getTvShowsPath() {
        return preferences.get(PATH_TVSHOWS_KEY, DEFAULT_TV);
    }

    public static void setOnlineMetadataApiKey(String apiKey) {
        preferences.put(ONLINE_METADATA_API_KEY, apiKey);
    }

    public static String getOnlineMetadataApiKey() {
        return preferences.get(ONLINE_METADATA_API_KEY, DEFAULT_APIKEY);
    }

    public static void setEnabledOnlineMetadataSearch(boolean enabled) {
        preferences.putBoolean(ENABLE_ONLINE_METADATA_SEARCH_KEY, enabled);
    }

    public static boolean isEnabledOnlineMetadataSearch() {
        return preferences.getBoolean(ENABLE_ONLINE_METADATA_SEARCH_KEY, DEFAULT_ENABLE_METADATA_SEARCH);
    }
}
