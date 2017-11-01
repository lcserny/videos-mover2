package net.cserny.videosmover;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import net.cserny.videosmover.model.VideoRow;
import net.cserny.videosmover.service.MessageProvider;
import net.cserny.videosmover.service.MessageRegistry;
import net.cserny.videosmover.service.StaticPathsProvider;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Files;

import static org.junit.Assert.assertTrue;

/**
 * Created by leonardo on 02.09.2017.
 */
public class TestApplicationEndToEnd extends AbstractApplicationTest {
    @Autowired
    private MessageRegistry messageRegistry;

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        messageRegistry.getMessages().clear();
    }

    @Test
    public void givenNoDownloadsLocationWhenScanningThenShowPopup() throws Exception {
        StaticPathsProvider.setDownloadsPath(null);
        Button scanButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#scanButton").query();

        clickOn(scanButton);

        assertTrue(messageRegistry.getMessages().contains(MessageProvider.getIputMissing()));
    }

    @Test
    public void givenNoTvShowsOrMoviesLocationWhenScanningAndTryingToMoveShowsPopup() throws Exception {
        StaticPathsProvider.setTvShowsPath(null);
        Button scanButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#scanButton").query();
        Button moveButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#moveButton").query();

        clickOn(scanButton);
        Thread.sleep(100);
        Node movieCheckOnFirstRow = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieCheckOnFirstRow);
        clickOn(moveButton);

        assertTrue(messageRegistry.getMessages().contains(MessageProvider.getOutputMissing()));
    }

    @Test
    public void selectingNoVideoAfterScanningAndTryingToMoveShowsPopup() throws Exception {
        Button scanButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#scanButton").query();
        Button moveButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#moveButton").query();

        clickOn(scanButton);
        clickOn(moveButton);

        assertTrue(messageRegistry.getMessages().contains(MessageProvider.getNothingSelected()));
    }

    @Test
    public void whenApplicationScansCheckmarksAVideoAsMovieAndMovesItThenShowPopup() throws Exception {
        Button scanButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#scanButton").query();
        Button moveButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#moveButton").query();

        clickOn(scanButton);
        Thread.sleep(100);
        Node movieCheckOnFirstRow = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieCheckOnFirstRow);
        clickOn(moveButton);

        assertTrue(messageRegistry.getMessages().contains(MessageProvider.getMoveSuccessful()));
    }

    @Test
    public void afterMovingVideoMovieCleanupDownloads() throws Exception {
        Button scanButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#scanButton").query();
        Button moveButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#moveButton").query();
        TableView<VideoRow> tableView = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        Thread.sleep(100);
        VideoRow videoRow = tableView.getItems().get(0);
        Node movieCheckOnFirstRow = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieCheckOnFirstRow);
        clickOn(moveButton);

        assertTrue(!Files.exists(videoRow.getVideo().getInput().getParent()));
    }
}
