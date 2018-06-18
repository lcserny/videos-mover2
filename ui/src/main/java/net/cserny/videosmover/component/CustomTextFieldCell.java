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
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.*;
import net.cserny.videosmover.service.CachedMetadataService;
import net.cserny.videosmover.service.helper.SimpleVideoOutputHelper;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.CustomTextField;

import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomTextFieldCell extends TableCell<VideoRow, String> {

    private final CachedMetadataService metadataService;
    private final CustomTextField customTextField;
    private final Button button;

    private StringProperty boundProperty = null;
    private SimpleVideoOutput videoOutput;
    private Pattern valuePattern = Pattern.compile("(.*) \\((.*)\\)");

    public CustomTextFieldCell(CachedMetadataService metadataService) {
        this.metadataService = metadataService;

        button = initButton();
        customTextField = initCustomTextField();

        setGraphic(customTextField);
    }

    private void processForShow(String output) {
        button.setVisible(true);
        videoOutput = SimpleVideoOutputHelper.buildVideoOutput(output);
    }

    private void processForHide() {
        button.setVisible(false);
        videoOutput = null;
    }

    private CustomTextField initCustomTextField() {
        CustomTextField customTextField = new CustomTextField();
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
            new Thread(() -> {
                List<VideoMetadata> videoMetadataList = processVideoMetadataList();
                setImageToButton(button, altImage);
                PopOver popOver = buildPopover(videoMetadataList, mainImage, altImage);
                Platform.runLater(() -> togglePopover(button, popOver));
            }).start();
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
        customTextField.setText(SimpleVideoOutputHelper.formatOutput(videoOutput, videoMetadata));
        videoMetadata.setSelected(true);
        popOver.hide();
    }

    private List<VideoMetadata> processVideoMetadataList() {
        VideoQuery videoQuery = VideoQuery.newInstance().withName(videoOutput.getName()).build();
        return metadataService.searchMetadata(videoQuery, videoOutput.getVideoType());
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

        if (output != null && !output.isEmpty()) {
            processForShow(output);
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
            updateVideoRowOutput(outputProperty);
        }
        setContentDisplay(contentDisplay);
    }

    private void updateVideoRowOutput(SimpleStringProperty outputProperty) {
        VideoRow videoRow = getTableView().getItems().get(getIndex());
        String value = outputProperty.getValue();
        if (videoRow != null && value != null && !value.isEmpty()) {
            videoRow.setOutput(convertToPath(value));
        }
    }

    private VideoPath convertToPath(String manualPath) {
        Path path = StaticPathsProvider.getPath(manualPath);
        String outputPath = path.getParent().toString();
        String outputFolder = path.getFileName().toString();
        String year = "";

        Matcher matcher = valuePattern.matcher(outputFolder);
        if (matcher.find()) {
            outputFolder = matcher.group(1);
            year = matcher.group(2);
        }

        return new VideoPath(outputPath, outputFolder, year);
    }
}
