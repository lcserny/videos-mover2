package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.model.SimpleVideoOutput;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoPath;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.service.CachedMetadataService;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.service.helper.SimpleVideoOutputHelper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

@Singleton
public class CachedVideoRetriever implements VideoNameParser {

    private final CachedMetadataService cachedTmdbService;

    @Inject
    public CachedVideoRetriever(CachedMetadataService cachedTmdbService) {
        this.cachedTmdbService = cachedTmdbService;
    }

    @Override
    public void parseTvShow(VideoPath videoPath) {
        String internal = parseOutputInternal(videoPath.getOutputFolder(),
                StaticPathsProvider.getTvShowsPath(), CachedMetadataService.TVSHOW_PREFIX);
        videoPath.setOutputFolder(internal);
    }

    @Override
    public void parseMovie(VideoPath videoPath) {
        String internal = parseOutputInternal(videoPath.getOutputFolder(),
                StaticPathsProvider.getMoviesPath(), CachedMetadataService.MOVIE_PREFIX);
        videoPath.setOutputFolder(internal);
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
