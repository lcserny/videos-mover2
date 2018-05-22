package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.model.SimpleVideoOutput;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.service.CachedTmdbService;
import net.cserny.videosmover.service.StaticPathsProvider;
import net.cserny.videosmover.service.helper.SimpleVideoOutputHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public class CachedVideoRetriever implements VideoNameParser {

    private final CachedTmdbService cachedTmdbService;

    @Autowired
    public CachedVideoRetriever(CachedTmdbService cachedTmdbService) {
        this.cachedTmdbService = cachedTmdbService;
    }

    @Override
    public String parseTvShow(String output) {
        return parseOutputInternal(output, StaticPathsProvider.getTvShowsPath(), CachedTmdbService.TVSHOW_PREFIX);
    }

    @Override
    public String parseMovie(String output) {
        return parseOutputInternal(output, StaticPathsProvider.getMoviesPath(), CachedTmdbService.MOVIE_PREFIX);
    }

    private String parseOutputInternal(String output, String rootPath, String cachePrefix) {
        SimpleVideoOutput videoOutput = SimpleVideoOutputHelper.buildVideoOutput(rootPath + "/" + output);
        VideoQuery videoQuery = VideoQuery.newInstance().withName(videoOutput.getName()).withYear(videoOutput.getYear()).build();
        String formattedKey = cachedTmdbService.keyFormat(cachePrefix, videoQuery);
        String foundOutput = checkVideoCache(formattedKey);

        return foundOutput != null ? foundOutput : output;
    }

    private String checkVideoCache(String key) {
        Map<String, List<VideoMetadata>> videoCache = cachedTmdbService.getVideoCache();
        if (videoCache.containsKey(key)) {
            List<VideoMetadata> videoMetadataList = videoCache.get(key);
            for (VideoMetadata videoMetadata : videoMetadataList) {
                if (videoMetadata.isSelected()) {
                    return SimpleVideoOutputHelper.formatOutputWithoutPath(videoMetadata);
                }
            }
        }
        return null;
    }
}
