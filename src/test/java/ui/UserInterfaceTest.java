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
import org.junit.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

/**
 * Created by leonardo on 03.09.2017.
 */
// TODO: implement mocks for the services so we just test the UI
public class UserInterfaceTest extends ApplicationTest
{
    private Scene scene;

    @BeforeClass
    public static void setupClass() throws Exception {
        ApplicationTest.launch(MainApplication.class);
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.cleanupStages();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.scene = stage.getScene();
        stage.show();
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
        Thread.sleep(500);
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
        Thread.sleep(500);
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
        Thread.sleep(500);
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
        Thread.sleep(500);
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
        Thread.sleep(500);
        // while scanning check that loadingImage is visible
        assertFalse(loadingImage.isVisible());
    }

    @Test
    public void whenCheckingMovieInMainTableThenMovieOutputPathShouldNotBeEmpty() throws Exception {
        Button scanButton = from(scene.getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(scene.getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        Thread.sleep(500);
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
        Thread.sleep(500);
        Node tvShowCheckOnFirstRow = from(scene.getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(2).query();
        clickOn(tvShowCheckOnFirstRow);

        String videoOutput = tableView.getItems().get(0).getOutput();
        assertNotNull(videoOutput);
        assertNotEquals("", videoOutput);
    }
}
