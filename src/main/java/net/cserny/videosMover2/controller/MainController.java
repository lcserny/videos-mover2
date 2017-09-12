package net.cserny.videosMover2.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.dto.VideoRow;
import net.cserny.videosMover2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by leonardo on 02.09.2017.
 */
public class MainController implements Initializable
{
    public static final String INPUT_MISSING = "inputMissing";
    public static final String OUTPUT_MISSING = "outputMissing";
    public static final String NOTHING_SELECTED = "nothingSelected";
    public static final String MOVE_RESULT = "moveResult";

    @FXML
    private ImageView loadingImage;
    @FXML
    private TableView<VideoRow> tableView;
    @FXML
    private TextField downloadsPathTextField;
    @FXML
    private TextField moviePathTextField;
    @FXML
    private TextField tvShowPathTextField;
    @FXML
    private Button moveButton;

    private ScanService scanService;
    private VideoMover videoMover;
    private OutputNameResolver nameResolver;
    private VideoCleaner videoCleaner;
    private Scene scene;

    @Autowired
    public MainController(ScanService scanService, VideoMover videoMover, OutputNameResolver nameResolver, VideoCleaner videoCleaner) {
        this.scanService = scanService;
        this.videoMover = videoMover;
        this.nameResolver = nameResolver;
        this.videoCleaner = videoCleaner;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initDefaultPaths();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    private void initDefaultPaths() {
        downloadsPathTextField.setText(PathsProvider.getDownloadsPath());
        moviePathTextField.setText(PathsProvider.getMoviesPath());
        tvShowPathTextField.setText(PathsProvider.getTvShowsPath());
    }

    @SuppressWarnings("unchecked")
    private void initTable() {
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        for (TableColumn column : tableView.getColumns()) {
            switch (column.getId()) {
                case "nameCol":
                    TableColumn<VideoRow, String> nameCol = (TableColumn<VideoRow, String>) column;
                    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                    nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
                    break;
                case "movieCol":
                    TableColumn<VideoRow, Boolean> movieCol = (TableColumn<VideoRow, Boolean>) column;
                    movieCol.setCellValueFactory(new PropertyValueFactory<>("isMovie"));
                    movieCol.setCellFactory(param -> new CheckBoxTableCell<>());
                    break;
                case "tvshowCol":
                    TableColumn<VideoRow, Boolean> tvShowCol = (TableColumn<VideoRow, Boolean>) column;
                    tvShowCol.setCellValueFactory(new PropertyValueFactory<>("isTvShow"));
                    tvShowCol.setCellFactory(param -> new CheckBoxTableCell<>());
                    break;
                case "outputCol":
                    TableColumn<VideoRow, String> outputCol = (TableColumn<VideoRow, String>) column;
                    outputCol.setCellValueFactory(new PropertyValueFactory<>("output"));
                    outputCol.setCellFactory(TextFieldTableCell.forTableColumn());
                    outputCol.setOnEditCommit(event -> event.getTableView().getItems()
                            .get(event.getTablePosition().getRow()).setOutput(event.getNewValue()));
                    break;
            }
        }
    }

    public void loadTableView(ActionEvent event) throws IOException {
        if (PathsProvider.getDownloadsPath() == null) {
            showAlert(Alert.AlertType.ERROR, "Downloads folder doesn't exist, please set correct path and try again.", "Input Path Error", INPUT_MISSING);
            return;
        }

        loadingImage.setImage(new Image(getClass().getResourceAsStream("/images/loading.gif")));
        Runnable expensiveTask = () -> {
            try {
                List<Video> scannedVideos = scanService.scan(PathsProvider.getDownloadsPath());
                List<VideoRow> videoRowList = new ArrayList<>();
                for (Video video : scannedVideos) {
                    VideoRow videoRow = buildVideoRow(video);
                    videoRowList.add(videoRow);
                }
                tableView.setItems(FXCollections.observableList(videoRowList));
                moveButton.setDisable(videoRowList.isEmpty());
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadingImage.setImage(new Image(getClass().getResourceAsStream("/images/scan-button.png")));
        };
        new Thread(expensiveTask).start();
    }

    private VideoRow buildVideoRow(Video video) {
        VideoRow videoRow = new VideoRow(video);
        videoRow.setName(video.getInput().getFileName().toString());
        videoRow.isMovieProperty().addListener((observable, oldValue, newValue) -> {
            videoRow.setIsMovie(newValue);
            videoRow.setOutput(newValue ? nameResolver.resolve(videoRow.getVideo()) : "");
        });
        videoRow.isTvShowProperty().addListener((observable, oldValue, newValue) -> {
            videoRow.setIsTvShow(newValue);
            videoRow.setOutput(newValue ? nameResolver.resolve(videoRow.getVideo()) : "");
        });
        return videoRow;
    }

    public void moveVideos(ActionEvent event) throws IOException {
        if (PathsProvider.getMoviesPath() == null || PathsProvider.getTvShowsPath() == null) {
            showAlert(Alert.AlertType.ERROR, "Movies or TvShows folder/s not set, please set correct paths and try again.", "Output Path Error", OUTPUT_MISSING);
            return;
        }

        List<Video> selectedVideos = tableView.getItems().stream()
                .filter(videoRow -> videoRow.isTvShow() || videoRow.isMovie())
                .map(VideoRow::getVideo).collect(Collectors.toList());
        if (selectedVideos.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No video files have been selected, nothing was moved...", "No Move Done", NOTHING_SELECTED);
            return;
        }

        boolean result = videoMover.moveAll(selectedVideos);

        Alert.AlertType resultType = Alert.AlertType.WARNING;
        String resultMessage = "Problem occurred while moving, some files might not have been moved, please check";
        String resultTitle = "Move Error Detected";

        if (result) {
            videoCleaner.cleanAll(selectedVideos);
            resultType = Alert.AlertType.INFORMATION;
            resultMessage = "Selected video files have been moved successfully";
            resultTitle = "Move Successful";
        }

        showAlert(resultType, resultMessage, resultTitle, MOVE_RESULT);

        loadTableView(null);
    }

    public void setDownloadsPath(ActionEvent event) {
        String path = processDirectoryChooserPath("Choose Downloads folder", PathsProvider.getDownloadsPath());
        if (path != null) {
            downloadsPathTextField.setText(path);
            PathsProvider.setDownloadsPath(path);
        }
    }

    public void setMoviesPath(ActionEvent event) {
        String path = processDirectoryChooserPath("Choose Movies folder", PathsProvider.getMoviesPath());
        if (path != null) {
            moviePathTextField.setText(path);
            PathsProvider.setMoviesPath(path);
        }
    }

    public void setTvShowsPath(ActionEvent event) {
        String path = processDirectoryChooserPath("Choose TvShows folder", PathsProvider.getTvShowsPath());
        if (path != null) {
            tvShowPathTextField.setText(path);
            PathsProvider.setTvShowsPath(path);
        }
    }

    private void showAlert(Alert.AlertType type, String content, String title, Object userData) {
        Alert alert = new Alert(type, content);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.initOwner(scene.getWindow());
        alert.setOnShown(e -> { scene.setUserData(userData); });
        alert.setOnHidden(e -> { scene.setUserData(null); });
        alert.show();
    }

    private String processDirectoryChooserPath(String title, String currentPathString) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        if (currentPathString != null) {
            chooser.setInitialDirectory(new File(currentPathString));
        }
        File chosenPath = chooser.showDialog(scene.getWindow());
        if (chosenPath != null && chosenPath.exists()) {
            return chosenPath.getAbsolutePath();
        }
        return null;
    }
}
