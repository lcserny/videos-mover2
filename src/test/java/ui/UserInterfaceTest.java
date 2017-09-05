package ui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import net.cserny.videosMover2.MainApplication;
import net.cserny.videosMover2.dto.VideoRow;
import net.cserny.videosMover2.service.SystemPathsProvider;
import org.junit.Ignore;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

/**
 * Created by leonardo on 03.09.2017.
 */
public class UserInterfaceTest extends ApplicationTest
{
    private Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        MainApplication application = new MainApplication();
        application.start(stage);
        scene = stage.getScene();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.cleanupStages();
        FxToolkit.hideStage();
    }

    @Test
    public void givenScanButtonAndMainTableWhenClickedThenAppPopulatesMainTableWithVideosFound() throws Exception {
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();
        TableView tableView = from(scene.getRoot()).lookup("#tableView").query();

        clickOn(scanButton);

        assertFalse(tableView.getItems().isEmpty());
    }

    @Test
    public void givenSetDownloadsPathButtonWhenClickedSetsSystemPathForDownloads() throws Exception {
        Button setDownloadsButton = from(scene.getRoot()).lookup("#setDownloadsButton").query();
        TextField downloadsTextField = from(scene.getRoot()).lookup("#downloadsPathTextField").query();

        clickOn(setDownloadsButton);
        push(KeyCode.ENTER);

        assertEquals(SystemPathsProvider.getDownloadsPath(), downloadsTextField.getText());
    }

    @Test
    public void givenSetMoviesPathButtonWhenClickedSetsSystemPathForMovies() throws Exception {
        Button setMoviesButton = from(scene.getRoot()).lookup("#setMoviesButton").query();
        TextField moviesTextField = from(scene.getRoot()).lookup("#moviePathTextField").query();

        clickOn(setMoviesButton);
        push(KeyCode.ENTER);

        assertEquals(SystemPathsProvider.getMoviesPath(), moviesTextField.getText());
    }

    @Test
    public void givenSetTvShowsPathButtonWhenClickedSetsSystemPathForTvShows() throws Exception {
        Button setTvShowsButton = from(scene.getRoot()).lookup("#setTvShowsButton").query();
        TextField tvShowsTextField = from(scene.getRoot()).lookup("#tvShowPathTextField").query();

        clickOn(setTvShowsButton);
        push(KeyCode.ENTER);

        assertEquals(SystemPathsProvider.getTvShowsPath(), tvShowsTextField.getText());
    }

    @Test
    public void givenPopulatedMainTableWhenUnCheckingCheckedMovieThenTvShowShouldAlsoBeUnChecked() throws Exception {
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(scene.getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        VideoRow videoRow = tableView.getItems().get(0);

        Node movieCheckOnFirstRow = from(scene.getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieCheckOnFirstRow);

        Node movieUnCheckOnFirstRow = from(scene.getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieUnCheckOnFirstRow);
        assertFalse(videoRow.isTvShow());
    }

    @Test
    public void givenPopulatedMainTableWhenUnCheckingCheckedTvShowThenMovieShouldAlsoBeUnChecked() throws Exception {
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(scene.getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        VideoRow videoRow = tableView.getItems().get(0);

        Node tvShowCheckOnFirstRow = from(scene.getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(2).query();
        clickOn(tvShowCheckOnFirstRow);

        Node tvShowUnCheckOnFirstRow = from(scene.getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(2).query();
        clickOn(tvShowUnCheckOnFirstRow);
        assertFalse(videoRow.isMovie());
    }

    @Test
    public void givenPopulatedMainTableWhenCheckingTvShowCheckedMovieThenUnCheckTvShowForIt() throws Exception {
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(scene.getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        Node tvShowCheckOnFirstRow = from(scene.getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(2).query();
        clickOn(tvShowCheckOnFirstRow);

        Node movieCheckOnFirstRow = from(scene.getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieCheckOnFirstRow);

        assertFalse(tableView.getItems().get(0).isTvShow());
    }

    @Test
    public void givenPopulatedMainTableWhenCheckingMovieCheckedTvShowThenUnCheckMovieForIt() throws Exception {
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(scene.getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        Node movieCheckOnFirstRow = from(scene.getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieCheckOnFirstRow);

        Node tvShowCheckOnFirstRow = from(scene.getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(2).query();
        clickOn(tvShowCheckOnFirstRow);

        assertFalse(tableView.getItems().get(0).isMovie());
    }

    @Ignore("Don't know how to do this..., maybe just check if setVisible(true) was called on image?")
    @Test
    public void whenClickingOnScanButtonShowLoadingImageUntilMainTableIsPopulated() throws Exception {
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(scene.getRoot()).lookup("#tableView").query();
        ImageView loadingImage = from(scene.getRoot()).lookup("#loadingImage").query();

        assertFalse(loadingImage.isVisible());
        clickOn(scanButton);
        // while scanning check that loadingImage is visible
        assertFalse(loadingImage.isVisible());
    }

    @Test
    public void whenCheckingMovieInMainTableThenMovieOutputPathShouldNotBeEmpty() throws Exception {
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(scene.getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        Node movieCheckOnFirstRow = from(scene.getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieCheckOnFirstRow);

        String videoOutput = tableView.getItems().get(0).getOutput();
        assertNotNull(videoOutput);
        assertNotEquals("", videoOutput);
    }

    @Test
    public void whenCheckingTvShowInMainTableThenTvShowOutputPathShouldNotBeEmpty() throws Exception {
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(scene.getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        Node tvShowCheckOnFirstRow = from(scene.getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(2).query();
        clickOn(tvShowCheckOnFirstRow);

        String videoOutput = tableView.getItems().get(0).getOutput();
        assertNotNull(videoOutput);
        assertNotEquals("", videoOutput);
    }
}
