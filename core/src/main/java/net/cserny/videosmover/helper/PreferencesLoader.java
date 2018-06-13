package net.cserny.videosmover.helper;

import java.util.prefs.Preferences;

public class PreferencesLoader {

    private static final Preferences preferences;

    static {
        preferences = Preferences.userNodeForPackage(PreferencesLoader.class);
    }

    public static String getDownloadsPath() {
        return preferences.get(PropertiesLoader.PATH_DOWNLOADS_KEY, PropertiesLoader.getDownloadsPath());
    }

    public static void setDownloadsPath(String downloadsPath) {
        preferences.put(PropertiesLoader.PATH_DOWNLOADS_KEY, downloadsPath);
    }

    public static String getMoviesPath() {
        return preferences.get(PropertiesLoader.PATH_MOVIES_KEY, PropertiesLoader.getMoviesPath());
    }

    public static void setMoviesPath(String moviesPath) {
        preferences.put(PropertiesLoader.PATH_MOVIES_KEY, moviesPath);
    }

    public static String getTvShowsPath() {
        return preferences.get(PropertiesLoader.PATH_TVSHOWS_KEY, PropertiesLoader.getTvShowsPath());
    }

    public static void setTvShowsPath(String tvShowsPath) {
        preferences.put(PropertiesLoader.PATH_TVSHOWS_KEY, tvShowsPath);
    }
}
