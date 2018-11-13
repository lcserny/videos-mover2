package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoDate;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.service.CachedMetadataService;
import net.cserny.videosmover.service.helper.SimpleVideoOutputHelper;
import net.cserny.videosmover.service.observer.VideoAdjustmentObserver;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class CachedVideoRetriever implements VideoNameParser {

    private final CachedMetadataService cachedTmdbService;
    private static final Pattern releaseDatePattern = Pattern.compile("(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})");

    @Inject
    public CachedVideoRetriever(CachedMetadataService cachedTmdbService) {
        this.cachedTmdbService = cachedTmdbService;
    }

    @Override
    public void parseTvShow(Video video, List<VideoAdjustmentObserver> observers) {
        String internal = parseOutputInternal(video, StaticPathsProvider.getTvShowsPath(),
                CachedMetadataService.TVSHOW_PREFIX);
        video.setOutputFolderName(internal);
    }

    @Override
    public void parseMovie(Video video, List<VideoAdjustmentObserver> observers) {
        String internal = parseOutputInternal(video, StaticPathsProvider.getMoviesPath(),
                CachedMetadataService.MOVIE_PREFIX);
        video.setOutputFolderName(internal);
    }

    private String parseOutputInternal(Video video, String rootPath, String cachePrefix) {
        VideoQuery videoQuery = VideoQuery.newInstance()
                .withName(video.getOutputFolderName())
                .withYear(video.getDate().getYear())
                .build();
        String formattedKey = cachedTmdbService.keyFormat(cachePrefix, videoQuery);
        String foundOutput = checkVideoCache(formattedKey, video);

        return foundOutput != null ? foundOutput : video.getOutputFolderName();
    }

    private String checkVideoCache(String key, Video video) {
        Map<String, List<VideoMetadata>> videoCache = cachedTmdbService.getVideoCache();
        if (videoCache.containsKey(key)) {
            List<VideoMetadata> videoMetadataList = videoCache.get(key);
            for (VideoMetadata videoMetadata : videoMetadataList) {
                if (videoMetadata.isSelected()) {
                    populateYear(video, videoMetadata);
                    return videoMetadata.getName();
                }
            }
        }
        return null;
    }

    private void populateYear(Video video, VideoMetadata videoMetadata) {
        if (!StringUtils.isEmpty(videoMetadata.getReleaseDate())) {
            Matcher matcher = releaseDatePattern.matcher(videoMetadata.getReleaseDate());
            if (matcher.find()) {
                VideoDate date = video.getDate();
                date.setYear(Integer.valueOf(matcher.group("year")));
                date.setMonth(Integer.valueOf(matcher.group("month")));
                date.setDay(Integer.valueOf(matcher.group("day")));
            }
        }
    }
}
