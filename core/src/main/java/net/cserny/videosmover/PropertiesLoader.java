package net.cserny.videosmover;

import com.google.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertiesLoader {

    private static final Logger LOGGER = Logger.getLogger(PropertiesLoader.class.getName());

    private static final String PATH_DOWNLOADS_KEY = "path.downloads";
    private static final String PATH_MOVIES_KEY = "path.movies";
    private static final String PATH_TVSHOWS_KEY = "path.tvshows";
    private static final String TMDB_API_KEY = "tmdb.api.key";
    private static final String SIMILARITY_PERCENT_KEY = "similarity.percent";
    private static final String MIN_VIDEO_SIZE_KEY = "minimum.video.size";
    private static final String VIDEO_EXCLUDE_PATHS_KEY = "video.exclude.paths";
    private static final String VIDEO_MIME_TYPES_KEY = "video.mime.types";
    private static final String RESTRICTED_REMOVE_PATHS_KEY = "restricted.remove.paths";
    private static final String SUBTITLE_EXTENSIONS_KEY = "video.subtitle.extensions";
    private static final String VIDEO_TRIM_PARTS_KEY = "video.trim.parts";

    private static Properties application = new Properties();

    static {
        initApplication();
    }

    private static void initApplication() {
        URL url = Resources.getResource("application.properties");
        try (InputStream inputStream = Resources.asByteSource(url).openBufferedStream()) {
            application.load(inputStream);
        } catch (IOException e) {
            LOGGER.severe("Failed to load application.properties!");
        }
    }

    public static String getTmdbApiKey() {
        return getString(TMDB_API_KEY);
    }

    public static String getDownloadsPath() {
        return getString(PATH_DOWNLOADS_KEY);
    }

    public static String getMoviesPath() {
        return getString(PATH_MOVIES_KEY);
    }

    public static String getTvShowsPath() {
        return getString(PATH_TVSHOWS_KEY);
    }

    public static List<String> getSubtitleExtensions() {
        return getStringList(SUBTITLE_EXTENSIONS_KEY, ",");
    }

    public static List<String> getNameTrimParts() {
        return getStringList(VIDEO_TRIM_PARTS_KEY, ";");
    }

    public static List<String> getRestrictedFolders() {
        return getStringList(RESTRICTED_REMOVE_PATHS_KEY, ",");
    }

    public static List<String> getVideoExcludePaths() {
        return getStringList(VIDEO_EXCLUDE_PATHS_KEY, ",");
    }

    public static List<String> getVideoMimeTypes() {
        return getStringList(VIDEO_MIME_TYPES_KEY, ",");
    }

    public static long getMinimumVideoSize() {
        Optional<String> fromEnv = getFromEnv(MIN_VIDEO_SIZE_KEY);
        if (fromEnv.isPresent()) {
            return Long.valueOf(fromEnv.get());
        }

        String propVal = application.getProperty(MIN_VIDEO_SIZE_KEY);
        if (propVal != null && !propVal.isEmpty()) {
            return Long.valueOf(propVal);
        }

        throw new RuntimeException("Environment does not contain any value for key: " + MIN_VIDEO_SIZE_KEY);
    }

    public static int getSimilarityPercent() {
        Optional<String> fromEnv = getFromEnv(SIMILARITY_PERCENT_KEY);
        if (fromEnv.isPresent()) {
            return Integer.valueOf(fromEnv.get());
        }

        String propVal = application.getProperty(SIMILARITY_PERCENT_KEY);
        if (propVal != null && !propVal.isEmpty()) {
            return Integer.valueOf(propVal);
        }

        throw new RuntimeException("Environment does not contain any value for key: " + SIMILARITY_PERCENT_KEY);
    }

    private static List<String> getStringList(String key, String splitRegex) {
        Optional<String> fromEnv = getFromEnv(key);
        if (fromEnv.isPresent()) {
            return Arrays.asList(fromEnv.get().split(splitRegex));
        }

        String propVal = application.getProperty(key);
        if (propVal != null && !propVal.isEmpty()) {
            return Arrays.asList(propVal.split(splitRegex));
        }

        throw new RuntimeException("Environment does not contain any value for key: " + key);
    }

    private static String getString(String key) {
        Optional<String> fromEnv = getFromEnv(key);
        if (fromEnv.isPresent()) {
            return fromEnv.get();
        }

        String propVal = application.getProperty(key);
        if (propVal != null && !propVal.isEmpty()) {
            return propVal;
        }

        throw new RuntimeException("Environment does not contain any value for key: " + key);
    }

    private static Optional<String> getFromEnv(String key) {
        String envVal = System.getenv(key);
        if (envVal != null && !envVal.isEmpty()) {
            return Optional.of(envVal);
        }

        envVal = System.getenv(key.replaceAll("\\.", "_").toUpperCase());
        if (envVal != null && !envVal.isEmpty()) {
            return Optional.of(envVal);
        }

        return Optional.empty();
    }
}
