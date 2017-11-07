package net.cserny.videosmover.controller;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import net.cserny.videosmover.component.CustomTextFieldCell;
import net.cserny.videosmover.model.Message;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoRow;
import net.cserny.videosmover.provider.MainStageProvider;
import net.cserny.videosmover.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
public class MainController implements Initializable {
    @FXML
    private ImageView loadingImage;
    @FXML
    private Pane settingsPane;
    @FXML
    private TableView<VideoRow> tableView;
    @FXML
    private TextField downloadsPathTextField, moviePathTextField, tvShowPathTextField;
    @FXML
    private Button moveButton, scanButton, setDownloadsButton, setMoviesButton, setTvShowsButton;

    private final MessageRegistry messageRegistry;
    private final ScanService scanService;
    private final VideoMover videoMover;
    private final VideoCleaner videoCleaner;
    private final MainStageProvider stageProvider;
    private final OutputResolver outputResolver;
    private final VideoMetadataService metadataService;

    @Autowired
    public MainController(ScanService scanService, VideoMover videoMover, VideoCleaner videoCleaner, MessageRegistry messageRegistry,
                          MainStageProvider stageProvider, OutputResolver outputResolver, VideoMetadataService metadataService) {
        this.stageProvider = stageProvider;
        this.messageRegistry = messageRegistry;
        this.scanService = scanService;
        this.videoMover = videoMover;
        this.videoCleaner = videoCleaner;
        this.outputResolver = outputResolver;
        this.metadataService = metadataService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initTable();
        initDefaultPaths();
        initSlidingSettingsPane();
    }

    private void initSlidingSettingsPane() {
        int translationValue = -480;
        settingsPane.setTranslateX(translationValue);

        TranslateTransition slidingSettingsTranslation = new TranslateTransition(Duration.millis(250), settingsPane);
        slidingSettingsTranslation.setFromX(translationValue);
        slidingSettingsTranslation.setToX(0);

        settingsPane.setOnMouseClicked(event -> {
            Boolean open = (Boolean) settingsPane.getUserData();
            if (open != null && open) {
                slidingSettingsTranslation.setRate(-1);
                slidingSettingsTranslation.play();
                settingsPane.setUserData(false);
            } else {
                slidingSettingsTranslation.setRate(1);
                slidingSettingsTranslation.play();
                settingsPane.setUserData(true);
            }
        });
    }

    private void initButtons() {
        addPostActionHandler(scanButton);
        addPostActionHandler(moveButton);
        addPostActionHandler(setDownloadsButton);
        addPostActionHandler(setMoviesButton);
        addPostActionHandler(setTvShowsButton);
    }

    private void addPostActionHandler(Button button) {
        EventHandler<ActionEvent> onAction = button.getOnAction();
        button.setOnAction(event -> {
            onAction.handle(event);
            messageRegistry.displayMessages();
        });
    }

    private void initDefaultPaths() {
        downloadsPathTextField.setText(StaticPathsProvider.getDownloadsPath());
        moviePathTextField.setText(StaticPathsProvider.getMoviesPath());
        tvShowPathTextField.setText(StaticPathsProvider.getTvShowsPath());
    }

    @SuppressWarnings("unchecked")
    private void initTable() {
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        for (TableColumn<VideoRow, ?> column : tableView.getColumns()) {
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
                    outputCol.setCellFactory(param -> new CustomTextFieldCell(metadataService));
                    break;
            }
        }
    }

    public void loadTableView(ActionEvent event) throws IOException {
        if (StaticPathsProvider.getDownloadsPath() == null) {
            messageRegistry.add(MessageProvider.getIputMissing());
            return;
        }

        loadingImage.setImage(new Image(getClass().getResourceAsStream("/images/loading.gif")));
        Runnable expensiveTask = () -> {
            try {
                List<Video> scannedVideos = scanService.scan(StaticPathsProvider.getDownloadsPath());
                List<VideoRow> videoRowList = new ArrayList<>();
                for (int i = 0; i < scannedVideos.size(); i++) {
                    Video video = scannedVideos.get(i);
                    VideoRow videoRow = buildVideoRow(i, video);
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

    private VideoRow buildVideoRow(int index, Video video) {
        VideoRow videoRow = new VideoRow(index, video);
        videoRow.setName(video.getInput().getFileName().toString());
        videoRow.isMovieProperty().addListener((observable, oldValue, checkmarkValue) -> {
            videoRow.setIsMovie(checkmarkValue);
            videoRow.setOutput(checkmarkValue ? outputResolver.resolve(videoRow.getVideo()) : "");
        });
        videoRow.isTvShowProperty().addListener((observable, oldValue, checkmarkValue) -> {
            videoRow.setIsTvShow(checkmarkValue);
            videoRow.setOutput(checkmarkValue ? outputResolver.resolve(videoRow.getVideo()) : "");
        });
        return videoRow;
    }

    public void moveVideos(ActionEvent event) throws IOException {
        if (StaticPathsProvider.getMoviesPath() == null || StaticPathsProvider.getTvShowsPath() == null) {
            messageRegistry.add(MessageProvider.getOutputMissing());
            return;
        }

        List<Video> selectedVideos = tableView.getItems().stream().filter(videoRow -> videoRow.isTvShow() || videoRow.isMovie())
                .map(VideoRow::getVideo).collect(Collectors.toList());
        if (selectedVideos.isEmpty()) {
            messageRegistry.add(MessageProvider.getNothingSelected());
            return;
        }

        boolean result = videoMover.moveAll(selectedVideos);
        Message message = MessageProvider.getProblemOccurred();
        if (result) {
            videoCleaner.cleanAll(selectedVideos);
            message = MessageProvider.getMoveSuccessful();
        }
        messageRegistry.add(message);
        loadTableView(null);
    }

    public void setDownloadsPath(ActionEvent event) {
        String path = processDirectoryChooserPath("Choose Downloads folder", StaticPathsProvider.getDownloadsPath());
        if (path != null) {
            downloadsPathTextField.setText(path);
            StaticPathsProvider.setDownloadsPath(path);
        }
    }

    public void setMoviesPath(ActionEvent event) {
        String path = processDirectoryChooserPath("Choose Movies folder", StaticPathsProvider.getMoviesPath());
        if (path != null) {
            moviePathTextField.setText(path);
            StaticPathsProvider.setMoviesPath(path);
        }
    }

    public void setTvShowsPath(ActionEvent event) {
        String path = processDirectoryChooserPath("Choose TvShows folder", StaticPathsProvider.getTvShowsPath());
        if (path != null) {
            tvShowPathTextField.setText(path);
            StaticPathsProvider.setTvShowsPath(path);
        }
    }

    private String processDirectoryChooserPath(String title, String currentPathString) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        if (currentPathString != null) {
            chooser.setInitialDirectory(new File(currentPathString));
        }
        File chosenPath = chooser.showDialog(stageProvider.getStage());
        if (chosenPath != null && chosenPath.exists()) {
            return chosenPath.getAbsolutePath();
        }
        return null;
    }
}
