import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cserny.videosMover2.MainApplication;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        Button scanButton = from(root).lookup(".scan-button").query();
        TableView tableView = from(root).lookup("#tableView").query();

        clickOn(scanButton);

        assertFalse(tableView.getItems().isEmpty());
    }
}
