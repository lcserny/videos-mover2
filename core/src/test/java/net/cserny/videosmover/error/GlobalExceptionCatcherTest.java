//package net.cserny.videosmover.error;
//
//import net.cserny.videosmover.ApplicationConfig;
//import net.cserny.videosmover.service.SimpleMessageRegistry;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.hamcrest.Matchers.containsString;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {ApplicationConfig.class})
//public class GlobalExceptionCatcherTest {
//    private static String DISPLAYED_MESSAGE;
//
//    @Autowired
//    private GlobalExceptionCatcher globalExceptionCatcher;
//    @Autowired
//    private SimpleMessageRegistry messageRegistry;
//
//    @Before
//    public void setUp() throws Exception {
//        messageRegistry.registerDisplayProvider(message -> { DISPLAYED_MESSAGE = message.getContent(); });
//        Thread.setDefaultUncaughtExceptionHandler(globalExceptionCatcher);
//    }
//
//    @Test
//    public void uncaughtException_whenExceptionIsThrownAnywhere() throws Exception {
//        String exceptionMessage = "Some exception";
//        Thread testThread = new Thread(() -> { throw new RuntimeException(exceptionMessage); });
//        testThread.start();
//        testThread.join();
//        Assert.assertThat(DISPLAYED_MESSAGE, containsString(exceptionMessage));
//    }
//}
