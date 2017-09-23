package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.model.SimpleVideoOutput;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.service.CachedTmdbService;
import net.cserny.videosmover.service.PathsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leonardo on 10.09.2017.
 */
@Service
@Order(2)
// TODO: refactor this
public class CachedVideoRetriever implements VideoNameParser {
    private final CachedTmdbService cachedTmdbService;

    @Autowired
    public CachedVideoRetriever(CachedTmdbService cachedTmdbService) {
        this.cachedTmdbService = cachedTmdbService;
    }

    @Override
    public String parseTvShow(String output) {
        SimpleVideoOutput videoOutput = buildVideoOutput(PathsProvider.getTvShowsPath() + "/" + output);

        Map<String, List<VideoMetadata>> videoCache = cachedTmdbService.getVideoCache();
        VideoQuery videoQuery = VideoQuery.newInstance().withName(videoOutput.getName()).withYear(videoOutput.getYear()).build();
        String formattedKey = cachedTmdbService.keyFormat(CachedTmdbService.TVSHOW_PREFIX, videoQuery);

        if (videoCache.containsKey(formattedKey)) {
            List<VideoMetadata> videoMetadataList = videoCache.get(formattedKey);
            for (VideoMetadata videoMetadata : videoMetadataList) {
                if (videoMetadata.isSelected()) {
                    return String.format("%s (%s)", videoMetadata.getName(), videoMetadata.getReleaseDate());
                }
            }
        }

        return output;
    }

    @Override
    public String parseMovie(String output) {
        SimpleVideoOutput videoOutput = buildVideoOutput(PathsProvider.getMoviesPath() + "/" + output);

        Map<String, List<VideoMetadata>> videoCache = cachedTmdbService.getVideoCache();
        VideoQuery videoQuery = VideoQuery.newInstance().withName(videoOutput.getName()).withYear(videoOutput.getYear()).build();
        String formattedKey = cachedTmdbService.keyFormat(CachedTmdbService.MOVIE_PREFIX, videoQuery);

        if (videoCache.containsKey(formattedKey)) {
            List<VideoMetadata> videoMetadataList = videoCache.get(formattedKey);
            for (VideoMetadata videoMetadata : videoMetadataList) {
                if (videoMetadata.isSelected()) {
                    return String.format("%s (%s)", videoMetadata.getName(), videoMetadata.getReleaseDate());
                }
            }
        }

        return output;
    }

    private SimpleVideoOutput buildVideoOutput(String output) {
        String path = output.substring(0, output.lastIndexOf('/'));
        String name = output.substring(output.lastIndexOf('/') + 1);
        Matcher matcher = Pattern.compile("(\\d{4})").matcher(name);
        Integer year = null;
        if (matcher.find()) {
            name = name.substring(0, matcher.start() - 2);
            year = Integer.valueOf(matcher.group());
        }

        VideoType videoType = null;
        if (path.equals(PathsProvider.getMoviesPath())) {
            videoType = VideoType.MOVIE;
        } else if (path.equals(PathsProvider.getTvShowsPath())) {
            videoType = VideoType.TVSHOW;
        }

        return new SimpleVideoOutput(name, year, path, videoType);
    }
}
