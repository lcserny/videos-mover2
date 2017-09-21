package net.cserny.videosmover.controller;

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
import javafx.stage.DirectoryChooser;
import net.cserny.videosmover.listener.ChangeListenerProvider;
import net.cserny.videosmover.model.Message;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoRow;
import net.cserny.videosmover.provider.MainStageProvider;
import net.cserny.videosmover.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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
@Controller
public class MainController implements Initializable {
    @FXML
    private ImageView loadingImage;
    @FXML
    private TableView<VideoRow> tableView;
    @FXML
    private TextField downloadsPathTextField, moviePathTextField, tvShowPathTextField;
    @FXML
    private Button moveButton, scanButton, setDownloadsButton, setMoviesButton, setTvShowsButton;

    private MessageRegistry messageRegistry;
    private ScanService scanService;
    private VideoMover videoMover;
    private VideoCleaner videoCleaner;
    private MainStageProvider stageProvider;
    private ChangeListenerProvider changeListenerProvider;

    @Autowired
    public MainController(ScanService scanService, VideoMover videoMover, VideoCleaner videoCleaner, MessageRegistry messageRegistry,
                          MainStageProvider stageProvider, ChangeListenerProvider changeListenerProvider) {
        this.stageProvider = stageProvider;
        this.messageRegistry = messageRegistry;
        this.scanService = scanService;
        this.videoMover = videoMover;
        this.videoCleaner = videoCleaner;
        this.changeListenerProvider = changeListenerProvider;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initTable();
        initDefaultPaths();
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
            messageRegistry.add(MessageProvider.getIputMissing());
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
        videoRow.isMovieProperty().addListener(changeListenerProvider.getMovieChangeListener(videoRow));
        videoRow.isTvShowProperty().addListener(changeListenerProvider.getTvShowChangeListener(videoRow));
        return videoRow;
    }

    public void moveVideos(ActionEvent event) throws IOException {
        if (PathsProvider.getMoviesPath() == null || PathsProvider.getTvShowsPath() == null) {
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
