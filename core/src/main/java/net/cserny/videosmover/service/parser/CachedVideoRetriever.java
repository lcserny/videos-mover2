package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.service.CachedMetadataService;
import net.cserny.videosmover.service.observer.VideoAdjustmentObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Order(1)
@Component
public class CachedVideoRetriever implements VideoNameParser {

    private final CachedMetadataService cachedMetadataService;

    @Autowired
    public CachedVideoRetriever(CachedMetadataService cachedMetadataService) {
        this.cachedMetadataService = cachedMetadataService;
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
        String formattedKey = cachedMetadataService.keyFormat(cachePrefix, videoQuery);
        findInVideoCache(formattedKey).ifPresent(videoMetadata -> {
            video.setOutputFolderWithoutDate(videoMetadata.getName());
            video.setDateFromReleaseDate(videoMetadata.getReleaseDate());
        });
    }

    private Optional<VideoMetadata> findInVideoCache(String key) {
        List<VideoMetadata> videoMetadataList = cachedMetadataService.videoCache.get(key);
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
