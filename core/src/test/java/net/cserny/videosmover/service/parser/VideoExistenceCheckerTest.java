package net.cserny.videosmover.service.parser;

import net.cserny.videosmover.CoreTestComponent;
import net.cserny.videosmover.DaggerCoreTestComponent;
import net.cserny.videosmover.helper.InMemoryFileSystem;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.VideoPath;
import net.cserny.videosmover.service.MessageProvider;
import net.cserny.videosmover.service.SimpleMessageRegistry;
import net.cserny.videosmover.service.observer.VideoExistenceObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.*;

public class VideoExistenceCheckerTest {

    @Inject
    VideoExistenceChecker existenceChecker;

    @Inject
    SimpleMessageRegistry messageRegistry;

    private InMemoryFileSystem inMemoryFileSystem;

    public VideoExistenceCheckerTest() {
        CoreTestComponent component = DaggerCoreTestComponent.create();
        component.inject(this);
    }

    @Before
    public void setUp() throws Exception {
        inMemoryFileSystem = InMemoryFileSystem.initFileSystem();
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

        VideoPath videoPath = new VideoPath(StaticPathsProvider.getMoviesPath(), "The Big Sick", "2017");
        existenceChecker.parseMovie(videoPath, Collections.emptyList());

        assertTrue(messageRegistry.getMessages().contains(MessageProvider.existingFolderFound(existingFolder)));
    }

    @Test
    public void checkingWithExistenceObserverUpdatesObserver() throws IOException {
        String existingPath = StaticPathsProvider.getMoviesPath();
        String existingFolder = "I Am Batman (2099)";
        inMemoryFileSystem.create(existingPath, existingFolder, null, 0);

        VideoExistenceObserver observer = new VideoExistenceObserver();
        VideoPath videoPath = new VideoPath(StaticPathsProvider.getMoviesPath(), "I Am Batman", "2099");
        existenceChecker.parseMovie(videoPath, Collections.singletonList(observer));

        assertFalse(observer.shouldAdjustPath());
    }
}