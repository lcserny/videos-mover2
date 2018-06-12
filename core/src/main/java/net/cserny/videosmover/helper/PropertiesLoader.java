package net.cserny.videosmover.helper;

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

    public static final String PATH_DOWNLOADS_KEY = "path.downloads";
    public static final String PATH_MOVIES_KEY = "path.movies";
    public static final String PATH_TVSHOWS_KEY = "path.tvshows";

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
        Optional<String> stringOptional = processString(SIMILARITY_PERCENT_KEY);
        if (!stringOptional.isPresent()) {
            throw new RuntimeException("Environment does not contain any value for key: " + MIN_VIDEO_SIZE_KEY);
        }
        return Long.valueOf(stringOptional.get());
    }

    public static int getSimilarityPercent() {
        Optional<String> stringOptional = processString(SIMILARITY_PERCENT_KEY);
        if (!stringOptional.isPresent()) {
            throw new RuntimeException("Environment does not contain any value for key: " + SIMILARITY_PERCENT_KEY);
        }
        return Integer.valueOf(stringOptional.get());
    }

    private static List<String> getStringList(String key, String splitRegex) {
        Optional<String> stringOptional = processString(key);
        if (!stringOptional.isPresent()) {
            throw new RuntimeException("Environment does not contain any value for key: " + key);
        }
        return Arrays.asList(stringOptional.get().split(splitRegex));
    }

    private static String getString(String key) {
        return processString(key).orElseThrow(() -> new RuntimeException(
                        "Environment does not contain any value for key: " + key));
    }

    private static Optional<String> processString(String key) {
        Optional<String> fromEnv = getFromEnv(key);
        if (fromEnv.isPresent()) {
            return fromEnv;
        }

        String propVal = application.getProperty(key);
        if (propVal != null && !propVal.isEmpty()) {
            return Optional.of(propVal);
        }

        return Optional.empty();
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
