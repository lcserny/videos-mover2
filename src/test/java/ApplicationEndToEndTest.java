import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import net.cserny.videosMover2.MainApplication;
import net.cserny.videosMover2.controller.MainController;
import net.cserny.videosMover2.dto.VideoRow;
import net.cserny.videosMover2.service.SystemPathsProvider;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

/**
 * Created by leonardo on 02.09.2017.
 */
public class ApplicationEndToEndTest extends ApplicationTest
{
    private Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        MainApplication application = new MainApplication();
        application.start(stage);
        this.scene = stage.getScene();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.cleanupStages();
        FxToolkit.hideStage();
    }

    @Test
    public void givenNoDownloadsLocationWhenScanningThenShowPopup() throws Exception {
        SystemPathsProvider.setDownloadsPath(null);
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();

        clickOn(scanButton);

        assertEquals(MainController.INPUT_MISSING, scene.getUserData());
    }

    @Test
    public void givenNoTvShowsOrMoviesLocationWhenScanningAndTryingToMoveShowsPopup() throws Exception {
        SystemPathsProvider.setTvShowsPath(null);
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();
        Button moveButton = from(scene.getRoot()).lookup("#moveButton").query();
        TableView<VideoRow> tableView = from(scene.getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
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
        TableView<VideoRow> tableView = from(scene.getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        clickOn(moveButton);

        assertEquals(MainController.NOTHING_SELECTED, scene.getUserData());
    }

    @Test
    public void applicationScansCheckmarksAVideoAsMovieAndMovesItProperly() throws Exception {
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();
        Button moveButton = from(scene.getRoot()).lookup("#moveButton").query();
        TableView<VideoRow> tableView = from(scene.getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        Node movieCheckOnFirstRow = from(scene.getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieCheckOnFirstRow);
        clickOn(moveButton);

        // expecting no popup triggered and no exception thrown
        assertEquals(null, scene.getUserData());
    }
}
