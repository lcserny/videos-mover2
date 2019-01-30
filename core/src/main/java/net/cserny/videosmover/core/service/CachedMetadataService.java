package net.cserny.videosmover.core.service;

import net.cserny.videosmover.core.helper.ApplicationPreferences;
import net.cserny.videosmover.core.helper.StringHelper;
import net.cserny.videosmover.core.model.Video;
import net.cserny.videosmover.core.model.VideoMetadata;
import net.cserny.videosmover.core.model.VideoQuery;
import net.cserny.videosmover.core.model.VideoType;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public interface CachedMetadataService {

    Map<String, List<VideoMetadata>> videoCache = new HashMap<>(50);
    AtomicBoolean apiKeyChanged = new AtomicBoolean();
    String NO_API_KEY = "<CHANGE_ME>";
    String MOVIE_PREFIX = "MOVIE_";
    String TVSHOW_PREFIX = "TVSHOW_";
    int DEFAULT_CAST_SIZE = 5;
    int DEFAULT_VIDEOS_SIZE = 5;

    default boolean isEnabled() {
        boolean enabled = ApplicationPreferences.isEnabledOnlineMetadataSearch();
        String apiKey = ApplicationPreferences.getOnlineMetadataApiKey();
        return enabled && !StringHelper.isEmpty(apiKey) && !NO_API_KEY.equals(apiKey);
    }

    default String keyFormat(String prefix, VideoQuery query) {
        String formatted = prefix + query.getName();
        formatted = query.getYear() != null ? formatted + "_" + query.getYear() : formatted;
        formatted = Normalizer.normalize(formatted, Normalizer.Form.NFD);
        formatted = formatted.replaceAll("[^a-zA-Z0-9]]", "_");
        return formatted.toLowerCase();
    }

    default void setApiKeyChanged() {
        apiKeyChanged.set(true);
    }

    List<VideoMetadata> searchMetadata(VideoQuery query, VideoType type);

    void adjustOutputAndDate(Video video);
}
