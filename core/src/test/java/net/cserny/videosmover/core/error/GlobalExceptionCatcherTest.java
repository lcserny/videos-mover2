package net.cserny.videosmover.core.error;

import net.cserny.videosmover.core.CoreConfiguration;
import net.cserny.videosmover.core.service.SimpleMessageRegistry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.StringContains.containsString;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CoreConfiguration.class)
public class GlobalExceptionCatcherTest {

    private static String DISPLAYED_MESSAGE;

    @Autowired
    GlobalExceptionCatcher globalExceptionCatcher;

    @Autowired
    SimpleMessageRegistry messageRegistry;

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
