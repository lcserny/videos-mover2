package net.cserny.videosmover.component;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.cserny.videosmover.helper.StringHelper;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoMetadata;
import net.cserny.videosmover.model.VideoQuery;
import net.cserny.videosmover.model.VideoRow;
import net.cserny.videosmover.service.CachedMetadataService;
import net.cserny.videosmover.service.thread.TwoThreadsExecutor;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.CustomTextField;

import java.util.List;

import static net.cserny.videosmover.service.helper.VideoOutputHelper.trimReleaseDate;

public class CustomTextFieldCell extends TableCell<VideoRow, String> {

    private final CachedMetadataService metadataService;
    private final CustomTextField customTextField;
    private final Button button;

    private StringProperty boundProperty = null;
    private Video video;
    private boolean manuallyEditingTextField;

    public CustomTextFieldCell(CachedMetadataService metadataService) {
        this.metadataService = metadataService;

        button = initButton();
        customTextField = initCustomTextField();

        setGraphic(customTextField);
    }

    private void processForShow(Video video) {
        button.setVisible(true);
        this.video = video;
    }

    private void processForHide() {
        button.setVisible(false);
        video = null;
    }

    private CustomTextField initCustomTextField() {
        CustomTextField customTextField = new CustomTextField();
        customTextField.focusedProperty()
                .addListener((observable, oldValue, focused) -> { manuallyEditingTextField = focused; });
        customTextField.setRight(button);
        return customTextField;
    }

    private Button initButton() {
        Image mainImage = new Image(getClass().getResourceAsStream("/images/scan-button.png"));
        Image altImage = new Image(getClass().getResourceAsStream("/images/tmdb_logo_small.png"));
        Image loadingImage = new Image(getClass().getResourceAsStream("/images/loading.gif"));
        ImageView imageView = buildButtonImageView(mainImage);

        Button button = new Button("", imageView);
        button.getStyleClass().add("video-metadata-search-button");
        button.setVisible(false);
        button.setTooltip(new Tooltip("Search for video metadata online"));
        button.setOnAction(event -> {
            setImageToButton(button, loadingImage);
            TwoThreadsExecutor.doInAnotherThread(() -> {
                List<VideoMetadata> videoMetadataList = processVideoMetadataList();
                setImageToButton(button, altImage);
                PopOver popOver = buildPopover(videoMetadataList, mainImage, altImage);
                Platform.runLater(() -> togglePopover(button, popOver));
            });
        });

        return button;
    }

    private ImageView buildButtonImageView(Image mainImage) {
        ImageView imageView = new ImageView(mainImage);
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);
        return imageView;
    }

    private PopOver buildPopover(List<VideoMetadata> videoMetadataList, Image mainImage, Image altImage) {
        PopOver popOver = new PopOver();

        VBox vboxParent = new VBox();
        for (VideoMetadata videoMetadata : videoMetadataList) {
            HBox hbox = new HBox();
            hbox.getStyleClass().add("video-metadata-container");
            hbox.setOnMouseClicked(event -> handleHboxClick(videoMetadata, popOver));

            ImageView poster = new ImageView(new Image(videoMetadata.getPosterUrl()));
            VBox vbox = buildVideoInfoVbox(videoMetadata);
            hbox.getChildren().addAll(poster, vbox);

            vboxParent.getChildren().add(hbox);
        }

        popOver.setContentNode(vboxParent);
        popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);
        popOver.setOnHidden(event -> setImageToButton(button, mainImage));
        popOver.setOnShowing(event -> setImageToButton(button, altImage));

        return popOver;
    }

    private void setImageToButton(Button button, Image image) {
        ((ImageView) button.getGraphic()).setImage(image);
    }

    private VBox buildVideoInfoVbox(VideoMetadata videoMetadata) {
        Text title = new Text(videoMetadata.getName());
        title.getStyleClass().add("video-info-title");

        String metadataDescription = videoMetadata.getDescription();
        if (metadataDescription.length() > 450) {
            metadataDescription = metadataDescription.substring(0, 450) + "...";
        }
        Text description = new Text(metadataDescription);
        description.getStyleClass().add("video-info-description");
        description.setWrappingWidth(500);

        Text cast = new Text(String.join(", ", videoMetadata.getCast()));
        cast.getStyleClass().add("video-info-cast");
        cast.setWrappingWidth(500);

        VBox vbox = new VBox();
        vbox.getStyleClass().add("video-info-container");
        vbox.getChildren().addAll(title, description, cast);
        return vbox;
    }

    private void handleHboxClick(VideoMetadata videoMetadata, PopOver popOver) {
        video.setOutputFolderWithoutDate(videoMetadata.getName());
        video.setDateFromReleaseDate(videoMetadata.getReleaseDate());

        customTextField.setText(video.getOutputFolderWithDate());
        videoMetadata.setSelected(true);
        popOver.hide();
    }

    private List<VideoMetadata> processVideoMetadataList() {
        VideoQuery videoQuery = VideoQuery.newInstance()
                .withName(video.getOutputFolderWithoutDate())
                .withYear(video.getYear())
                .build();
        return metadataService.searchMetadata(videoQuery, video.getVideoType());
    }

    private void togglePopover(Button button, PopOver popOver) {
        if (popOver.isShowing()) {
            popOver.hide();
        } else {
            popOver.show(button);
        }
    }

    @Override
    protected void updateItem(String output, boolean empty) {
        super.updateItem(output, empty);
        int caretPosition = customTextField.getCaretPosition();

        VideoRow videoRow = null;
        if (!StringHelper.isEmpty(output)) {
            videoRow = getTableView().getItems().get(getIndex());
            processForShow(videoRow.getVideo());
        } else {
            processForHide();
        }

        ContentDisplay contentDisplay = ContentDisplay.TEXT_ONLY;
        if (!empty) {
            contentDisplay = ContentDisplay.GRAPHIC_ONLY;
            SimpleStringProperty outputProperty = (SimpleStringProperty) getTableColumn().getCellObservableValue(getIndex());
            if (boundProperty == null) {
                boundProperty = outputProperty;
                customTextField.textProperty().bindBidirectional(outputProperty);
            } else if (boundProperty != outputProperty) {
                customTextField.textProperty().unbindBidirectional(boundProperty);
                boundProperty = outputProperty;
                customTextField.textProperty().bindBidirectional(boundProperty);
            }
            updateVideoRowOutput(outputProperty, videoRow);
        }

        setContentDisplay(contentDisplay);
        customTextField.positionCaret(caretPosition);
    }

    private void updateVideoRowOutput(SimpleStringProperty outputProperty, VideoRow videoRow) {
        String outputFolderWithPossiblyDate = outputProperty.getValue();
        if (video != null && !StringHelper.isEmpty(outputFolderWithPossiblyDate)) {
            video.setOutputFolderWithoutDate(trimReleaseDate(outputFolderWithPossiblyDate));
            video.setDateFromReleaseDate(outputFolderWithPossiblyDate);
            if (videoRow != null) {
                if (manuallyEditingTextField) {
                    videoRow.setOutputManual(outputFolderWithPossiblyDate);
                } else {
                    videoRow.setOutput(video);
                }
            }
        }
    }
}
