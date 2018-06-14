package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.model.SimpleVideoOutput;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.service.CachedTmdbService;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.service.helper.SimpleVideoOutputHelper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class CachedVideoRetriever implements VideoNameParser {

    private final CachedTmdbService cachedTmdbService;

    @Inject
    public CachedVideoRetriever(CachedTmdbService cachedTmdbService) {
        this.cachedTmdbService = cachedTmdbService;
    }

    @Override
    public String parseTvShow(String resolvedName) {
        return parseOutputInternal(resolvedName, StaticPathsProvider.getTvShowsPath(), CachedTmdbService.TVSHOW_PREFIX);
    }

    @Override
    public String parseMovie(String resolvedName) {
        return parseOutputInternal(resolvedName, StaticPathsProvider.getMoviesPath(), CachedTmdbService.MOVIE_PREFIX);
    }

    private String parseOutputInternal(String output, String rootPath, String cachePrefix) {
        SimpleVideoOutput videoOutput = SimpleVideoOutputHelper.buildVideoOutput(rootPath + "/" + output);
        VideoQuery videoQuery = VideoQuery.newInstance().withName(videoOutput.getName()).withYear(videoOutput.getYear()).build();
        String formattedKey = cachedTmdbService.keyFormat(cachePrefix, videoQuery);

        // TODO: maybe don't do this here
        Optional<String> outputFound = checkVideoCache(formattedKey);
        if (outputFound.isPresent()) {
            return outputFound.get();
        }

        VideoMetadata metadata = new VideoMetadata();
        try {
            switch (cachePrefix) {
                case CachedTmdbService.MOVIE_PREFIX:
                    metadata = cachedTmdbService.searchMovieMetadata(videoQuery).get(0);
                    break;
                case CachedTmdbService.TVSHOW_PREFIX:
                    metadata = cachedTmdbService.searchTvShowMetadata(videoQuery).get(0);
                    break;
            }
        } catch (Exception ignored) {
            return output;
        }

        metadata.setSelected(true);
        return SimpleVideoOutputHelper.formatOutputWithoutPath(metadata);
    }

    private Optional<String> checkVideoCache(String key) {
        Map<String, List<VideoMetadata>> videoCache = cachedTmdbService.getVideoCache();
        if (videoCache.containsKey(key)) {
            for (VideoMetadata videoMetadata : videoCache.get(key)) {
                if (videoMetadata.isSelected()) {
                    return Optional.of(SimpleVideoOutputHelper.formatOutputWithoutPath(videoMetadata));
                }
            }
        }
        return Optional.empty();
    }
}
