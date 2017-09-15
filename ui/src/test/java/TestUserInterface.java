import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import net.cserny.videosMover.model.VideoRow;
import net.cserny.videosMover.service.PathsProvider;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by leonardo on 03.09.2017.
 */
public class TestUserInterface extends AbstractApplicationTest
{
    @Test
    public void givenScanButtonAndMainTableWhenClickedThenAppPopulatesMainTableWithVideosFound() throws Exception {
        Button scanButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#scanButton").query();
        TableView tableView = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView").query();

        clickOn(scanButton);

        assertFalse(tableView.getItems().isEmpty());
    }

    @Test
    public void givenSetDownloadsPathButtonWhenClickedSetsSystemPathForDownloads() throws Exception {
//        Button setDownloadsButton = from(scene.getRoot()).lookup("#setDownloadsButton").query();
        TextField downloadsTextField = from(stageProvider.getStage().getScene().getRoot()).lookup("#downloadsPathTextField").query();

//        clickOn(setDownloadsButton);
//        push(KeyCode.ENTER);
        downloadsTextField.setText(PathsProvider.getDownloadsPath());
        PathsProvider.setDownloadsPath(PathsProvider.getDownloadsPath());

        assertEquals(PathsProvider.getDownloadsPath(), downloadsTextField.getText());
    }

    @Test
    public void givenSetMoviesPathButtonWhenClickedSetsSystemPathForMovies() throws Exception {
//        Button setMoviesButton = from(scene.getRoot()).lookup("#setMoviesButton").query();
        TextField moviesTextField = from(stageProvider.getStage().getScene().getRoot()).lookup("#moviePathTextField").query();

//        clickOn(setMoviesButton);
//        push(KeyCode.ENTER);
        moviesTextField.setText(PathsProvider.getMoviesPath());
        PathsProvider.setMoviesPath(PathsProvider.getMoviesPath());

        assertEquals(PathsProvider.getMoviesPath(), moviesTextField.getText());
    }

    @Test
    public void givenSetTvShowsPathButtonWhenClickedSetsSystemPathForTvShows() throws Exception {
//        Button setTvShowsButton = from(scene.getRoot()).lookup("#setTvShowsButton").query();
        TextField tvShowsTextField = from(stageProvider.getStage().getScene().getRoot()).lookup("#tvShowPathTextField").query();

//        clickOn(setTvShowsButton);
//        push(KeyCode.ENTER);
        tvShowsTextField.setText(PathsProvider.getTvShowsPath());
        PathsProvider.setTvShowsPath(PathsProvider.getTvShowsPath());

        assertEquals(PathsProvider.getTvShowsPath(), tvShowsTextField.getText());
    }

    @Test
    public void givenPopulatedMainTableWhenUnCheckingCheckedMovieThenTvShowShouldAlsoBeUnChecked() throws Exception {
        Button scanButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        Thread.sleep(100);
        VideoRow videoRow = tableView.getItems().get(0);
        Node movieCheckOnFirstRow = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieCheckOnFirstRow);
        Node movieUnCheckOnFirstRow = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieUnCheckOnFirstRow);

        assertFalse(videoRow.isTvShow());
    }

    @Test
    public void givenPopulatedMainTableWhenUnCheckingCheckedTvShowThenMovieShouldAlsoBeUnChecked() throws Exception {
        Button scanButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        Thread.sleep(100);
        VideoRow videoRow = tableView.getItems().get(0);
        Node tvShowCheckOnFirstRow = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(2).query();
        clickOn(tvShowCheckOnFirstRow);
        Node tvShowUnCheckOnFirstRow = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(2).query();
        clickOn(tvShowUnCheckOnFirstRow);

        assertFalse(videoRow.isMovie());
    }

    @Test
    public void givenPopulatedMainTableWhenCheckingTvShowCheckedMovieThenUnCheckTvShowForIt() throws Exception {
        Button scanButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        Thread.sleep(100);
        Node tvShowCheckOnFirstRow = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(2).query();
        clickOn(tvShowCheckOnFirstRow);
        Node movieCheckOnFirstRow = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieCheckOnFirstRow);

        assertFalse(tableView.getItems().get(0).isTvShow());
    }

    @Test
    public void givenPopulatedMainTableWhenCheckingMovieCheckedTvShowThenUnCheckMovieForIt() throws Exception {
        Button scanButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        Thread.sleep(100);
        Node movieCheckOnFirstRow = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieCheckOnFirstRow);
        Node tvShowCheckOnFirstRow = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(2).query();
        clickOn(tvShowCheckOnFirstRow);

        assertFalse(tableView.getItems().get(0).isMovie());
    }

    @Ignore("Don't know how to do this..., maybe just check if setVisible(true) was called on image?")
    @Test
    public void whenClickingOnScanButtonShowLoadingImageUntilMainTableIsPopulated() throws Exception {
        Button scanButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView").query();
        ImageView loadingImage = from(stageProvider.getStage().getScene().getRoot()).lookup("#loadingImage").query();

        assertFalse(loadingImage.isVisible());
        clickOn(scanButton);
        Thread.sleep(100);
        // while scanning check that loadingImage is visible
        assertFalse(loadingImage.isVisible());
    }

    @Test
    public void whenCheckingMovieInMainTableThenMovieOutputPathShouldNotBeEmpty() throws Exception {
        Button scanButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        Thread.sleep(100);
        Node movieCheckOnFirstRow = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        clickOn(movieCheckOnFirstRow);

        String videoOutput = tableView.getItems().get(0).getOutput();
        assertNotNull(videoOutput);
        assertNotEquals("", videoOutput);
    }

    @Test
    public void whenCheckingTvShowInMainTableThenTvShowOutputPathShouldNotBeEmpty() throws Exception {
        Button scanButton = from(stageProvider.getStage().getScene().getRoot()).lookup("#scanButton").query();
        TableView<VideoRow> tableView = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView").query();

        clickOn(scanButton);
        Thread.sleep(100);
        Node tvShowCheckOnFirstRow = from(stageProvider.getStage().getScene().getRoot()).lookup("#tableView")
                .lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(2).query();
        clickOn(tvShowCheckOnFirstRow);

        String videoOutput = tableView.getItems().get(0).getOutput();
        assertNotNull(videoOutput);
        assertNotEquals("", videoOutput);
    }
}
