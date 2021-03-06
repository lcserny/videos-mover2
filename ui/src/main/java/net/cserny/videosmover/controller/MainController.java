package net.cserny.videosmover.controller;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import net.cserny.videosmover.component.CustomTextFieldCell;
import net.cserny.videosmover.component.RadioButtonTableCell;
import net.cserny.videosmover.facade.MainFacade;
import net.cserny.videosmover.helper.ApplicationPreferences;
import net.cserny.videosmover.helper.LoadingService;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.helper.StringHelper;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoRow;
import net.cserny.videosmover.model.VideoType;
import net.cserny.videosmover.provider.MainStageProvider;
import net.cserny.videosmover.service.CachedMetadataService;
import net.cserny.videosmover.service.MessageProvider;
import net.cserny.videosmover.service.SimpleMessageRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static net.cserny.videosmover.helper.LoadingService.MOVE_LOADING_KEY;
import static net.cserny.videosmover.helper.LoadingService.SCAN_LOADING_KEY;
import static net.cserny.videosmover.service.thread.TwoThreadsExecutor.doInAnotherThread;

@Controller
public class MainController implements Initializable {

    @FXML
    private CheckBox enableOnlineMetadataCheckbox;
    @FXML
    private ImageView loadingImage;
    @FXML
    private Pane settingsPane, settingsVisualHint;
    @FXML
    private TableView<VideoRow> tableView;
    @FXML
    private TextField downloadsPathTextField, moviePathTextField, tvShowPathTextField, onlineMetadataApiKey;
    @FXML
    private Button moveButton, scanButton, setDownloadsButton, setMoviesButton, setTvShowsButton;

    private final MainFacade facade;
    private final SimpleMessageRegistry messageRegistry;
    private final MainStageProvider stageProvider;
    private final CachedMetadataService metadataService;
    private final LoadingService loadingService;

    @Autowired
    public MainController(MainFacade facade, SimpleMessageRegistry messageRegistry, MainStageProvider stageProvider,
                          CachedMetadataService metadataService, LoadingService loadingService) {
        this.facade = facade;
        this.stageProvider = stageProvider;
        this.messageRegistry = messageRegistry;
        this.metadataService = metadataService;
        this.loadingService = loadingService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initDefaultPaths();
        initSlidingSettingsPane();
        initApiSettings();
    }

    private void initApiSettings() {
        String apiKey = ApplicationPreferences.getOnlineMetadataApiKey();
        if (!StringHelper.isEmpty(apiKey)) {
            onlineMetadataApiKey.setText(apiKey);
        }
        enableOnlineMetadataCheckbox.setSelected(ApplicationPreferences.isEnabledOnlineMetadataSearch());
    }

    public void onlineMetadataEnableChanged(ActionEvent event) {
        boolean selected = enableOnlineMetadataCheckbox.isSelected();
        ApplicationPreferences.setEnabledOnlineMetadataSearch(selected);
    }

    public void initLoading() {
        Region region = (Region) stageProvider.getStage().getScene()
                .lookup("#opaqueRegion");
        ProgressIndicator progressIndicator = (ProgressIndicator) stageProvider.getStage().getScene()
                .lookup("#moveProgress");

        loadingService.register(SCAN_LOADING_KEY, () -> {
            loadingImage.setImage(new Image(getClass().getResourceAsStream("/images/loading.gif")));
        }, () -> {
            loadingImage.setImage(new Image(getClass().getResourceAsStream("/images/scan-button.png")));
        });

        loadingService.register(MOVE_LOADING_KEY, () -> {
            region.setVisible(true);
            progressIndicator.setProgress(-1.0f);
            progressIndicator.setVisible(true);
        }, () -> {
            region.setVisible(false);
            progressIndicator.setVisible(false);
        });
    }

    public void onlineMetadataAPIKeyChanged(KeyEvent event) {
        String newApiKey = onlineMetadataApiKey.getText();
        ApplicationPreferences.setOnlineMetadataApiKey(newApiKey);
        metadataService.setApiKeyChanged();
    }

    private void initSlidingSettingsPane() {
        TranslateTransition openSettings = new TranslateTransition(Duration.millis(250), settingsPane);
        openSettings.setToX(0);
        TranslateTransition closeSettings = new TranslateTransition(Duration.millis(250), settingsPane);

        settingsVisualHint.setOnMouseClicked(event -> {
            settingsVisualHint.setScaleX(settingsVisualHint.getScaleX() == 1 ? -1 : 1);
            if (settingsPane.getTranslateX() != 0) {
                openSettings.play();
            } else {
                closeSettings.setToX(-(settingsPane.getWidth() - 20));
                closeSettings.play();
            }
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
            messageRegistry.displayMessage(MessageProvider.inputMissing());
            return;
        }

        loadingService.showLoading(SCAN_LOADING_KEY);
        doInAnotherThread(() -> {
            List<VideoRow> videoRowList = facade.scanVideos();
            tableView.setItems(FXCollections.observableList(videoRowList));
            moveButton.setDisable(videoRowList.isEmpty());
            loadingService.hideLoading(SCAN_LOADING_KEY);
        });
    }

    public void moveVideos(ActionEvent event) {
        if (StaticPathsProvider.getMoviesPath() == null || StaticPathsProvider.getTvShowsPath() == null) {
            messageRegistry.displayMessage(MessageProvider.outputMissing());
            return;
        }

        List<Video> selectedVideos = tableView.getItems().stream()
                .filter(videoRow -> videoRow.getVideoType() != VideoType.NONE)
                .map(VideoRow::getVideo)
                .collect(Collectors.toList());

        if (selectedVideos.isEmpty()) {
            messageRegistry.displayMessage(MessageProvider.nothingSelected());
            return;
        }

        loadingService.showLoading(MOVE_LOADING_KEY);
        doInAnotherThread(() -> {
            facade.moveVideos(selectedVideos);
            loadingService.hideLoading(MOVE_LOADING_KEY);
            loadTableView(event);
        });
    }

    public void setDownloadsPath(ActionEvent event) {
        processDirectoryChooserPath("Choose Downloads folder", StaticPathsProvider.getDownloadsPath())
                .ifPresent(path -> {
                    downloadsPathTextField.setText(path);
                    StaticPathsProvider.setDownloadsPath(path);
                    ApplicationPreferences.setDownloadsPath(path);
                });
    }

    public void setMoviesPath(ActionEvent event) {
        processDirectoryChooserPath("Choose Movies folder", StaticPathsProvider.getMoviesPath())
                .ifPresent(path -> {
                    moviePathTextField.setText(path);
                    StaticPathsProvider.setMoviesPath(path);
                    ApplicationPreferences.setMoviesPath(path);
                });
    }

    public void setTvShowsPath(ActionEvent event) {
        processDirectoryChooserPath("Choose TvShows folder", StaticPathsProvider.getTvShowsPath())
                .ifPresent(path -> {
                    tvShowPathTextField.setText(path);
                    StaticPathsProvider.setTvShowsPath(path);
                    ApplicationPreferences.setTvShowsPath(path);
                });
    }

    private Optional<String> processDirectoryChooserPath(String title, String currentPathString) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        if (currentPathString != null) {
            chooser.setInitialDirectory(new File(currentPathString));
        }
        File chosenPath = chooser.showDialog(stageProvider.getStage());
        if (chosenPath != null && chosenPath.exists()) {
            return Optional.of(chosenPath.getAbsolutePath());
        }
        return Optional.empty();
    }
}
