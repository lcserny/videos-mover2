import helper.InMemoryVideoFileSystemInitializer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cserny.videosMover.ApplicationConfig;
import net.cserny.videosMover.MainApplication;
import net.cserny.videosMover.controller.MainController;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public abstract class AbstractApplicationTest extends ApplicationTest
{
    protected Scene scene;

    @Autowired
    private ApplicationContext context;

    private InMemoryVideoFileSystemInitializer tempVideoInitializer = new InMemoryVideoFileSystemInitializer();

    @BeforeClass
    public static void setupClass() throws Exception {
        launch(MainApplication.class);
    }

    @Before
    public void setUp() throws Exception {
        tempVideoInitializer.setUp();
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.cleanupStages();
        tempVideoInitializer.tearDown();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setControllerFactory(context::getBean);

        this.scene = new Scene(loader.load());
        MainController controller = loader.getController();
        controller.setScene(scene);

        stage.setScene(scene);
        stage.setTitle("Downloads VideoMover");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/application.png")));
        stage.centerOnScreen();
        stage.show();
    }
}
