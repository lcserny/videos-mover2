package net.cserny.videosmover.error;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import net.cserny.videosmover.CoreModule;
import net.cserny.videosmover.service.SimpleMessageRegistry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.StringContains.containsString;

public class GlobalExceptionCatcherTest {

    private static String DISPLAYED_MESSAGE;

    @Inject
    GlobalExceptionCatcher globalExceptionCatcher;

    @Inject
    SimpleMessageRegistry messageRegistry;

    public GlobalExceptionCatcherTest() {
        Injector injector = Guice.createInjector(new CoreModule());
        injector.injectMembers(this);
    }

    @Before
    public void setUp() {
        messageRegistry.registerDisplayProvider(message -> {
            DISPLAYED_MESSAGE = message.getContent();
        });
        Thread.setDefaultUncaughtExceptionHandler(globalExceptionCatcher);
    }

    @Test
    public void uncaughtException_whenExceptionIsThrownAnywhere() throws Exception {
        String exceptionMessage = "Some exception";
        Thread testThread = new Thread(() -> {
            throw new RuntimeException(exceptionMessage);
        });
        testThread.start();
        testThread.join();
        Assert.assertThat(DISPLAYED_MESSAGE, containsString(exceptionMessage));
    }
}
