package net.cserny.videosmover.controller;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import net.cserny.videosmover.component.CustomTextFieldCell;
import net.cserny.videosmover.component.MessageRegistryButtonAction;
import net.cserny.videosmover.component.RadioButtonTableCell;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoPath;
import net.cserny.videosmover.model.VideoRow;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.provider.MainStageProvider;
import net.cserny.videosmover.service.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Singleton
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

    private final SimpleMessageRegistry messageRegistry;
    private final ScanService scanService;
    private final VideoMover videoMover;
    private final VideoCleaner videoCleaner;
    private final MainStageProvider stageProvider;
    private final OutputResolver outputResolver;
    private final CachedTmdbService metadataService;

    @Inject
    public MainController(ScanService scanService, VideoMover videoMover, VideoCleaner videoCleaner,
                          SimpleMessageRegistry messageRegistry, MainStageProvider stageProvider, OutputResolver outputResolver,
                          CachedTmdbService metadataService) {
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
        TranslateTransition openSettings = new TranslateTransition(Duration.millis(250), settingsPane);
        openSettings.setToX(0);
        TranslateTransition closeSettings = new TranslateTransition(Duration.millis(250), settingsPane);

        settingsPane.setOnMouseClicked(event -> {
            if (settingsPane.getTranslateX() != 0) {
                openSettings.play();
            } else {
                closeSettings.setToX(-(settingsPane.getWidth() - 20));
                closeSettings.play();
            }
        });
    }

    private void initButtons() {
        moveButton.setOnAction(new MessageRegistryButtonAction(this::moveVideos, messageRegistry));
        scanButton.setOnAction(new MessageRegistryButtonAction(this::loadTableView, messageRegistry));
        setDownloadsButton.setOnAction(new MessageRegistryButtonAction(this::setDownloadsPath, messageRegistry));
        setMoviesButton.setOnAction(new MessageRegistryButtonAction(this::setMoviesPath, messageRegistry));
        setTvShowsButton.setOnAction(new MessageRegistryButtonAction(this::setTvShowsPath, messageRegistry));
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
                case "typeCol":
                    TableColumn<VideoRow, VideoType> typeCol = (TableColumn<VideoRow, VideoType>) column;
                    typeCol.setCellValueFactory(new PropertyValueFactory<>("videoType"));
                    typeCol.setCellFactory(param -> new RadioButtonTableCell());
                    typeCol.setOnEditCommit(event -> event.getTableView().getItems()
                            .get(event.getTablePosition().getRow())
                            .setVideoType(event.getNewValue()));
                    break;
                case "outputCol":
                    TableColumn<VideoRow, String> outputCol = (TableColumn<VideoRow, String>) column;
                    outputCol.setCellValueFactory(new PropertyValueFactory<>("output"));
                    outputCol.setCellFactory(param -> new CustomTextFieldCell(metadataService));
                    break;
            }
        }
    }

    public void loadTableView(ActionEvent event) {
        if (StaticPathsProvider.getDownloadsPath() == null) {
            messageRegistry.add(MessageProvider.inputMissing());
            return;
        }

        loadingImage.setImage(new Image(getClass().getResourceAsStream("/images/loading.gif")));
        new Thread(() -> {
            List<VideoRow> videoRowList = processVideoRows();
            tableView.setItems(FXCollections.observableList(videoRowList));
            moveButton.setDisable(videoRowList.isEmpty());
            loadingImage.setImage(new Image(getClass().getResourceAsStream("/images/scan-button.png")));
        }).start();
    }

    private List<VideoRow> processVideoRows() {
        return scanService.scan(StaticPathsProvider.getDownloadsPath()).stream()
                .map(this::buildVideoRow)
                .collect(Collectors.toList());
    }

    private VideoRow buildVideoRow(Video video) {
        VideoRow videoRow = new VideoRow(video);
        videoRow.setName(video.getInputFilename());
        videoRow.videoTypeProperty().addListener((observable, oldValue, newValue) -> {
            handleCheckmark(video, newValue, videoRow);
        });
        return videoRow;
    }

    private void handleCheckmark(Video video, VideoType videoType, VideoRow videoRow) {
        video.setVideoType(videoType);
        videoRow.setVideoType(videoType);

        VideoPath videoPath = VideoPath.emptyVideoPath;
        if (videoType != VideoType.NONE) {
            videoPath = outputResolver.resolve(videoRow.getVideo());
        }

        videoRow.setOutput(StaticPathsProvider.getPath(videoPath).toString());
        video.setOutputPath(StaticPathsProvider.getPath(videoPath.getOutputPath()));
        video.setOutputFolderName(videoPath.getOutputFolder());
    }

    public void moveVideos(ActionEvent event) {
        if (StaticPathsProvider.getMoviesPath() == null || StaticPathsProvider.getTvShowsPath() == null) {
            messageRegistry.add(MessageProvider.outputMissing());
            return;
        }

        List<Video> selectedVideos = tableView.getItems().stream()
                .filter(videoRow -> videoRow.getVideoType() != VideoType.NONE && videoRow.getVideoType() != null)
                .map(VideoRow::getVideo)
                .collect(Collectors.toList());

        if (selectedVideos.isEmpty()) {
            messageRegistry.add(MessageProvider.nothingSelected());
            return;
        }

        boolean result = videoMover.move(selectedVideos);
        if (result) {
            videoCleaner.clean(selectedVideos);
        }
        messageRegistry.add(result ? MessageProvider.moveSuccessful() : MessageProvider.problemOccurred());

        loadTableView(event);
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
