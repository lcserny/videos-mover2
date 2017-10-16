package net.cserny.videosmover;

import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.helper.TestHelperConfig;
import net.cserny.videosmover.helper.VideoCreationHelper;
import net.cserny.videosmover.model.SimpleVideoOutput;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.service.OutputResolver;
import net.cserny.videosmover.service.PathsProvider;
import net.cserny.videosmover.service.VideoMetadataService;
import net.cserny.videosmover.service.VideoMover;
import net.cserny.videosmover.service.helper.SimpleVideoOutputHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class, TestHelperConfig.class})
public class TestVideoMoving extends InMemoryVideoFileSystemInitializer {
    @Autowired
    private VideoCreationHelper videoHelper;
    @Autowired
    private VideoMover videoMover;
    @Autowired
    private VideoMetadataService metadataService;
    @Autowired
    private OutputResolver outputResolver;

    @Test
    public void givenVideoRowTvShowWhenMovingThenMoveToTvShowsOutput() throws Exception {
        Video video = videoHelper.createTvShow(DOWNLOADS_TVSHOW);
        assertTrue(videoMover.move(video));
    }

    @Test
    public void givenMultipleVideoRowsTvShowWhenMovingThenMoveAllToTvShowOutput() throws Exception {
        Video video1 = videoHelper.createTvShow(DOWNLOADS_TVSHOW);
        Video video2 = videoHelper.createTvShow(DOWNLOADS_EXISTING_TVSHOW);
        List<Video> videoList = Arrays.asList(video1, video2);

        assertTrue(videoMover.moveAll(videoList));
    }

    @Test
    public void givenVideoRowMovieWithSubtitlesWhenMovingThenMoveToMoviesOutputWithSubtitles() throws Exception {
        Video video = videoHelper.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE);
        video.setSubtitles(Collections.singletonList(PathsProvider.getPath(DOWNLOADS_SUBTITLE)));

        assertTrue(videoMover.move(video));
    }

    @Test
    public void whenSubtitlesAreInSubsFolderMoveThemToSubsFolderInOutputAlso() throws Exception {
        Video video = videoHelper.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE_IN_SUBS);
        Path subPath1 = PathsProvider.getPath(DOWNLOADS_SUBTITLE_IN_SUBS);
        Path subPath2 = PathsProvider.getPath(DOWNLOADS_SUBTITLE_IN_SUBS_IDX);
        video.setSubtitles(Arrays.asList(subPath1, subPath2));

        assertTrue(videoMover.move(video));
        assertTrue(Files.exists(video.getOutput().resolve("Subs").resolve(subPath1.getFileName())));
        assertTrue(Files.exists(video.getOutput().resolve("Subs").resolve(subPath2.getFileName())));
    }

    @Test
    public void whenMovieIsChosenFromMetadataServiceOnMovingPlaceMovieInCorrectOutput() throws Exception {
        Video video = videoHelper.createMovie(DOWNLOADS_MOVIE_WITH_SUBTITLE_IN_SUBS);
        SimpleVideoOutput output = SimpleVideoOutputHelper.buildVideoOutput(outputResolver.resolve(video));
        List<VideoMetadata> metadataList = metadataService.searchMovieMetadata(VideoQuery.newInstance()
                .withName(output.getName()).withYear(output.getYear()).build());
        VideoMetadata videoMetadata = metadataList.get(0);
        Assert.assertNotNull(videoMetadata);

        String formattedOutput = SimpleVideoOutputHelper.formatOutput(output, videoMetadata);
        Path outputPath = PathsProvider.getPath(formattedOutput);
        video.setOutput(outputPath);

        videoMover.move(video);
        Assert.assertTrue(Files.exists(outputPath));
    }
}
