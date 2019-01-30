package net.cserny.videosmover.core.service.parser;

import net.cserny.videosmover.core.CoreConfiguration;
import net.cserny.videosmover.core.helper.InMemoryFileSystem;
import net.cserny.videosmover.core.helper.StaticPathsProvider;
import net.cserny.videosmover.core.model.Video;
import net.cserny.videosmover.core.model.VideoType;
import net.cserny.videosmover.core.service.MessageProvider;
import net.cserny.videosmover.core.service.SimpleMessageRegistry;
import net.cserny.videosmover.core.service.observer.VideoExistenceObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

import static net.cserny.videosmover.core.helper.StaticPathsProvider.joinPaths;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CoreConfiguration.class)
public class VideoExistenceCheckerTest {

    @Autowired
    VideoExistenceChecker existenceChecker;

    @Autowired
    SimpleMessageRegistry messageRegistry;

    private String cachedMessage;
    private InMemoryFileSystem inMemoryFileSystem;

    @Before
    public void setUp() throws Exception {
        inMemoryFileSystem = new InMemoryFileSystem();
        messageRegistry.registerDisplayProvider(message -> {
            cachedMessage = message.getContent();
        });
    }

    @After
    public void tearDown() throws Exception {
        inMemoryFileSystem.closeFileSystem();
    }

    @Test
    public void findingExistingFolderAddsMessageToRegistry() throws IOException {
        String existingPath = StaticPathsProvider.getMoviesPath();
        String existingFolder = "The Big Sick (2017)";
        inMemoryFileSystem.create(existingPath, existingFolder, null, 0);

        String pathString = joinPaths(existingPath, existingFolder);
        Path path = StaticPathsProvider.getPath(pathString);
        Video video = new Video(null, path.toString());
        video.setVideoType(VideoType.MOVIE);
        video.setOutputFolderWithoutDate(existingFolder);

        existenceChecker.parseMovie(video, Collections.emptyList());

        assertEquals(MessageProvider.existingFolderFound(existingFolder).getContent(), cachedMessage);
    }

    @Test
    public void checkingWithExistenceObserverUpdatesObserver() throws IOException {
        String existingPath = StaticPathsProvider.getMoviesPath();
        String existingFolder = "I Am Batman (2099)";
        inMemoryFileSystem.create(existingPath, existingFolder, null, 0);

        String pathString = joinPaths(existingPath, existingFolder);
        Path path = StaticPathsProvider.getPath(pathString);
        Video video = new Video(null, path.toString());
        video.setVideoType(VideoType.MOVIE);
        video.setOutputFolderWithoutDate(existingFolder);

        VideoExistenceObserver observer = new VideoExistenceObserver();
        existenceChecker.parseMovie(video, Collections.singletonList(observer));

        assertFalse(observer.shouldAdjustPath());
    }
}