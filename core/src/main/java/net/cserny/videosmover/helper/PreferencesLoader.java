package net.cserny.videosmover.helper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.prefs.Preferences;

@Singleton
public class PreferencesLoader {

    private final Preferences preferences;

    @Inject
    public PreferencesLoader() {
        this.preferences = Preferences.userNodeForPackage(PreferencesLoader.class);
    }

    public String getDownloadsPath() {
        return preferences.get(PropertiesLoader.PATH_DOWNLOADS_KEY, PropertiesLoader.getDownloadsPath());
    }

    public void setDownloadsPath(String downloadsPath) {
        preferences.put(PropertiesLoader.PATH_DOWNLOADS_KEY, downloadsPath);
    }

    public String getMoviesPath() {
        return preferences.get(PropertiesLoader.PATH_MOVIES_KEY, PropertiesLoader.getMoviesPath());
    }

    public void setMoviesPath(String moviesPath) {
        preferences.put(PropertiesLoader.PATH_MOVIES_KEY, moviesPath);
    }

    public String getTvShowsPath() {
        return preferences.get(PropertiesLoader.PATH_TVSHOWS_KEY, PropertiesLoader.getTvShowsPath());
    }

    public void setTvShowsPath(String tvShowsPath) {
        preferences.put(PropertiesLoader.PATH_TVSHOWS_KEY, tvShowsPath);
    }
}
