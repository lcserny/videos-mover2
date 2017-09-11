import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import net.cserny.videosMover2.MainApplication;
import net.cserny.videosMover2.controller.MainController;
import net.cserny.videosMover2.service.PathsProvider;
import org.junit.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import service.TmpVideoInitializer;

import static org.junit.Assert.assertEquals;

/**
 * Created by leonardo on 02.09.2017.
 */
public class TestApplicationEndToEnd extends ApplicationTest
{
    private TmpVideoInitializer tempVideoInitializer = new TmpVideoInitializer();
    private Scene scene;

    @BeforeClass
    public static void setupClass() throws Exception {
        ApplicationTest.launch(MainApplication.class);
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
        this.scene = stage.getScene();
        stage.show();
    }

    @Test
    public void givenNoDownloadsLocationWhenScanningThenShowPopup() throws Exception {
        PathsProvider.setDownloadsPath(null);
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();

        clickOn(scanButton);

        assertEquals(MainController.INPUT_MISSING, scene.getUserData());
    }

    @Test
    public void givenNoTvShowsOrMoviesLocationWhenScanningAndTryingToMoveShowsPopup() throws Exception {
        PathsProvider.setTvShowsPath(null);
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();
        Button moveButton = from(scene.getRoot()).lookup("#moveButton").query();

        clickOn(scanButton);
        Thread.sleep(500);
        Node movieCheckOnFirstRow = from(scene.getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieCheckOnFirstRow);
        clickOn(moveButton);

        assertEquals(MainController.OUTPUT_MISSING, scene.getUserData());
    }

    @Test
    public void selectingNoVideoAfterScanningAndTryingToMoveShowsPopup() throws Exception {
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();
        Button moveButton = from(scene.getRoot()).lookup("#moveButton").query();

        clickOn(scanButton);
        clickOn(moveButton);

        assertEquals(MainController.NOTHING_SELECTED, scene.getUserData());
    }

    @Test
    public void whenApplicationScansCheckmarksAVideoAsMovieAndMovesItThenShowPopup() throws Exception {
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();
        Button moveButton = from(scene.getRoot()).lookup("#moveButton").query();

        clickOn(scanButton);
        Thread.sleep(500);
        Node movieCheckOnFirstRow = from(scene.getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieCheckOnFirstRow);
        clickOn(moveButton);

        assertEquals(MainController.MOVE_RESULT, scene.getUserData());
    }
}
