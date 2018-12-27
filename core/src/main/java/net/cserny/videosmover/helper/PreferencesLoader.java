package net.cserny.videosmover.helper;

import java.util.prefs.Preferences;

import static net.cserny.videosmover.helper.PropertiesLoader.*;

public class PreferencesLoader {

    private static final Preferences preferences = Preferences.userNodeForPackage(PreferencesLoader.class);

    public static String getDownloadsPath() {
        return preferences.get(PATH_DOWNLOADS_KEY, PropertiesLoader.getDownloadsPath());
    }

    public static void setDownloadsPath(String downloadsPath) {
        preferences.put(PATH_DOWNLOADS_KEY, downloadsPath);
    }

    public static String getMoviesPath() {
        return preferences.get(PATH_MOVIES_KEY, PropertiesLoader.getMoviesPath());
    }

    public static void setMoviesPath(String moviesPath) {
        preferences.put(PATH_MOVIES_KEY, moviesPath);
    }

    public static String getTvShowsPath() {
        return preferences.get(PATH_TVSHOWS_KEY, PropertiesLoader.getTvShowsPath());
    }

    public static void setTvShowsPath(String tvShowsPath) {
        preferences.put(PATH_TVSHOWS_KEY, tvShowsPath);
    }

    public static String getOnlineMetadataApiKey() {
        return preferences.get(ONLINE_METADATA_API_KEY, PropertiesLoader.getOnlineMetadataApiKey());
    }

    public static void setOnlineMetadataApiKey(String apiKey) {
        preferences.put(ONLINE_METADATA_API_KEY, apiKey);
    }

    public static boolean isEnabledOnlineMetadataSearch() {
        String bool = preferences.get(ENABLE_ONLINE_METADATA_SEARCH_KEY, getEnabledOnlineMetadataSearch());
        return Boolean.valueOf(bool);
    }

    public static void setEnabledOnlineMetadataSearch(boolean enabled) {
        preferences.put(ENABLE_ONLINE_METADATA_SEARCH_KEY, String.valueOf(enabled));
    }
}
