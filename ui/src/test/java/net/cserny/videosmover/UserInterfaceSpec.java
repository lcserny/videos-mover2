package net.cserny.videosmover;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import net.cserny.videosmover.error.GlobalExceptionCatcher;
import net.cserny.videosmover.helper.InMemoryVideoFileSystemInitializer;
import net.cserny.videosmover.provider.MainStageProvider;
import net.cserny.videosmover.service.ScanService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javax.annotation.PostConstruct;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by leonardo on 02.09.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
public class UserInterfaceSpec extends ApplicationTest {
    private InMemoryVideoFileSystemInitializer videoFileSystemInitializer;

    @Autowired
    private ConfigurableApplicationContext context;

    @SpyBean
    private ScanService scanService;

    @PostConstruct
    public void initFilesystem() {
        videoFileSystemInitializer = new InMemoryVideoFileSystemInitializer();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent parent = loader.load();
        Thread.setDefaultUncaughtExceptionHandler(context.getBean(GlobalExceptionCatcher.class));

        stage.setScene(new Scene(parent));
        stage.setTitle(MainApplication.TITLE);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/application.png")));
        stage.centerOnScreen();
        stage.show();

        context.getBean(MainStageProvider.class).setStage(stage);
    }

    @BeforeClass
    public static void setupHeadlessMode() {
        if (!Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
    }

    @Before
    public void setUp() throws Exception {
        videoFileSystemInitializer.setUp();
    }

    @After
    public void tearDown() throws Exception {
        videoFileSystemInitializer.tearDown();
        FxToolkit.hideStage();
        release(new KeyCode[0]);
        release(new MouseButton[0]);
    }

    @Test
    public void whenSearchButtonThenRunScanService() throws Exception {
        clickOn("#scanButton");
        verify(scanService).scan(any(String.class));
    }
}
