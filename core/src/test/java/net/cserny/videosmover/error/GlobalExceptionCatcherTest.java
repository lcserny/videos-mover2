package net.cserny.videosmover.error;

import net.cserny.videosmover.DaggerTestCoreComponent;
import net.cserny.videosmover.TestCoreComponent;

import net.cserny.videosmover.service.SimpleMessageRegistry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Inject;

import static org.hamcrest.core.StringContains.containsString;

public class GlobalExceptionCatcherTest {

    private static String DISPLAYED_MESSAGE;

    @Inject
    GlobalExceptionCatcher globalExceptionCatcher;

    @Inject
    SimpleMessageRegistry messageRegistry;

    @Before
    public void setUp() {
        TestCoreComponent component = DaggerTestCoreComponent.create();
        component.inject(this);

        messageRegistry.registerDisplayProvider(message -> { DISPLAYED_MESSAGE = message.getContent(); });
        Thread.setDefaultUncaughtExceptionHandler(globalExceptionCatcher);
    }

    @Test
    public void uncaughtException_whenExceptionIsThrownAnywhere() throws Exception {
        String exceptionMessage = "Some exception";
        Thread testThread = new Thread(() -> { throw new RuntimeException(exceptionMessage); });
        testThread.start();
        testThread.join();
        Assert.assertThat(DISPLAYED_MESSAGE, containsString(exceptionMessage));
    }
}
