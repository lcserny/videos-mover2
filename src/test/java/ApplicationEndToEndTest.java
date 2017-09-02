import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cserny.videosMover2.MainApplication;
import net.cserny.videosMover2.service.SystemPathsProvider;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

/**
 * Created by leonardo on 02.09.2017.
 */
public class ApplicationEndToEndTest extends ApplicationTest
{
    private Parent root;

    @Override
    public void start(Stage stage) throws Exception {
        root = new MainApplication().getRootNode();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void givenScanButtonAndMainTableWhenClickedThenAppPopulatesMainTableWithVideosFound() throws Exception {
        Button scanButton = from(root).lookup("#scanButton").query();
        TableView tableView = from(root).lookup("#tableView").query();

        clickOn(scanButton);

        assertFalse(tableView.getItems().isEmpty());
    }

//    @Test
//    public void givenSetDownloadsPathButtonWhenClickedSetsSystemPathForDownloads() throws Exception {
//        Button setDownloadsButton = from(root).lookup(".downloads-button").query();
//
//        clickOn(setDownloadsButton);
//        // file chooser is opened
//        // set path of file chooser
//        // get File from chooser
//
//        assertEquals("", SystemPathsProvider.getDownloadsPath());
//    }

    // given populated main table with videos, when clicking on Movie for a video, should resolve movie path correctly

    // given populated main table with videos, when clicking on TvShow for a video, should resolve tvShow path correctly
}
