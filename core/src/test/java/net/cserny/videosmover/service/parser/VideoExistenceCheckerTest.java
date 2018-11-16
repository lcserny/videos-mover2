package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.CoreTestComponent;
import net.cserny.videosmover.DaggerCoreTestComponent;
import net.cserny.videosmover.helper.InMemoryFileSystem;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.service.MessageProvider;
import net.cserny.videosmover.service.SimpleMessageRegistry;
import net.cserny.videosmover.service.observer.VideoExistenceObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

import static net.cserny.videosmover.helper.StaticPathsProvider.joinPaths;
import static org.junit.Assert.*;

public class VideoExistenceCheckerTest {

    @Inject
    VideoExistenceChecker existenceChecker;

    @Inject
    SimpleMessageRegistry messageRegistry;

    private String cachedMessage;
    private InMemoryFileSystem inMemoryFileSystem;

    public VideoExistenceCheckerTest() {
        CoreTestComponent component = DaggerCoreTestComponent.create();
        component.inject(this);
    }

    @Before
    public void setUp() throws Exception {
        inMemoryFileSystem = InMemoryFileSystem.initFileSystem();
        messageRegistry.registerDisplayProvider(message -> { cachedMessage = message.getContent(); });
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