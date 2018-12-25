package net.cserny.videosmover.service.parser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.service.CachedMetadataService;
import net.cserny.videosmover.service.observer.VideoAdjustmentObserver;

import java.util.List;
import java.util.Optional;

@Singleton
public class CachedVideoRetriever implements VideoNameParser {

    private final CachedMetadataService cachedTmdbService;

    @Inject
    public CachedVideoRetriever(CachedMetadataService cachedTmdbService) {
        this.cachedTmdbService = cachedTmdbService;
    }

    @Override
    public void parseTvShow(Video video, List<VideoAdjustmentObserver> observers) {
        parse(video, CachedMetadataService.TVSHOW_PREFIX);
    }

    @Override
    public void parseMovie(Video video, List<VideoAdjustmentObserver> observers) {
        parse(video, CachedMetadataService.MOVIE_PREFIX);
    }

    private void parse(Video video, String cachePrefix) {
        VideoQuery videoQuery = VideoQuery.newInstance()
                .withName(video.getOutputFolderWithoutDate())
                .withYear(video.getYear())
                .build();
        String formattedKey = cachedTmdbService.keyFormat(cachePrefix, videoQuery);
        findInVideoCache(formattedKey).ifPresent(videoMetadata -> {
            video.setOutputFolderWithoutDate(videoMetadata.getName());
            video.setDateFromReleaseDate(videoMetadata.getReleaseDate());
        });
    }

    private Optional<VideoMetadata> findInVideoCache(String key) {
        List<VideoMetadata> videoMetadataList = cachedTmdbService.getVideoCache().get(key);
        if (videoMetadataList != null) {
            for (VideoMetadata videoMetadata : videoMetadataList) {
                if (videoMetadata.isSelected()) {
                    return Optional.of(videoMetadata);
                }
            }
        }
        return Optional.empty();
    }
}
