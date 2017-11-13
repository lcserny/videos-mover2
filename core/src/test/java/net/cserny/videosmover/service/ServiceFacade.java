package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.*;

@Component
class ServiceFacade {
    @Autowired
    private VideoMover videoMover;
    @Autowired
    private CachedTmdbService metadataService;
    @Autowired
    private VideoCleaner videoCleaner;
    @Autowired
    private VideoChecker videoChecker;
    @Autowired
    private SubtitlesFinder subtitlesFinder;
    @Autowired
    private ScanService scanService;
    @Autowired
    private OutputResolver outputResolver;

    void assertMovingVideos(List<Video> videoList, Path movedPath) throws IOException {
        assertTrue(videoMover.move(videoList));
        for (Video movedVideo : videoList) {
            assertTrue(Files.exists(movedPath.resolve(movedVideo.getOutput())));
        }
    }

    void assertMovingSubtitles(Video movedVideo, Path movedPath, boolean includeSubs) {
        if (movedVideo.getSubtitles() != null && !movedVideo.getSubtitles().isEmpty()) {
            for (Path subtitlePath : movedVideo.getSubtitles()) {
                Path movedVideoSubtitlePath = includeSubs
                        ? movedPath.resolve(movedVideo.getOutput().getFileName()).resolve("Subs").resolve(subtitlePath.getFileName())
                        : movedPath.resolve(movedVideo.getOutput().getFileName()).resolve(subtitlePath.getFileName());
                assertTrue(Files.exists(movedVideoSubtitlePath));
            }
        }
    }

    void assertMovieMetadata(VideoQuery query, String name, String year) throws Exception {
        List<VideoMetadata> videoMetadataList = metadataService.searchMovieMetadata(query);
        assertVideoMetadata(query, videoMetadataList, name, (metadata) -> {
            Assert.assertTrue(metadata.getReleaseDate().contains(year));
        });
    }

    void assertTvShowMetadata(VideoQuery query, String name) throws Exception {
        List<VideoMetadata> videoMetadataList = metadataService.searchTvShowMetadata(query);
        assertVideoMetadata(query, videoMetadataList, name, null);
    }

    private void assertVideoMetadata(VideoQuery query, List<VideoMetadata> metadataList, String name, Consumer<VideoMetadata> callback) {
        if (query.getName() == null) {
            Assert.assertTrue(metadataList.isEmpty());
            return;
        }

        Assert.assertFalse(metadataList.isEmpty());
        VideoMetadata metadata = metadataList.get(0);

        Assert.assertEquals(name, metadata.getName());
        Assert.assertFalse(metadata.getDescription().isEmpty());
        Assert.assertFalse(metadata.getPosterUrl().isEmpty());
        Assert.assertFalse(metadata.getCast().isEmpty());

        if (callback != null) {
            callback.accept(metadata);
        }
    }

    void assertCleaning(Video video, boolean removeExpected) throws IOException {
        boolean moveSuccessful = videoMover.move(video);
        videoCleaner.clean(video);

        assertTrue(moveSuccessful);
        if (removeExpected) {
            assertFalse(Files.exists(video.getInput().getParent()));
        } else {
            assertTrue(Files.exists(video.getInput().getParent()));
        }
    }

    void assertVideo(String pathString, boolean shouldBeVideo) throws IOException {
        Assert.assertEquals(videoChecker.isVideo(StaticPathsProvider.getPath(pathString)), shouldBeVideo);
    }

    void assertSubtitlesFound(String pathString, boolean emptySubtitles) throws IOException {
        Path videoPath = StaticPathsProvider.getPath(pathString);
        List<Path> subtitles = subtitlesFinder.find(videoPath);
        assertEquals(subtitles.isEmpty(), emptySubtitles);
    }

    List<Video> assertScannedVideos(String location, boolean shouldBeEmpty, String subsPath) throws IOException {
        List<Video> videosScanned = scanService.scan(location);
        assertNotNull(videosScanned);
        assertEquals(videosScanned.isEmpty(), shouldBeEmpty);

        for (Video video : videosScanned) {
            if (subsPath != null && video.getInput().toString().contains(subsPath)) {
                List<Path> subtitles = video.getSubtitles();
                assertNotNull(subtitles);
                assertFalse(subtitles.isEmpty());
                assertEquals(1, subtitles.size());
            }
        }

        return videosScanned;
    }

    Video createTvShow(String input) {
        Video video = new Video();
        video.setIsTvShow(true);
        video.setInput(StaticPathsProvider.getPath(input));
        video.setOutput(StaticPathsProvider.getPath(outputResolver.resolve(video)));
        return video;
    }

    Video createMovie(String input) {
        Video video = new Video();
        video.setIsMovie(true);
        video.setInput(StaticPathsProvider.getPath(input));
        video.setOutput(StaticPathsProvider.getPath(outputResolver.resolve(video)));
        return video;
    }
}
