package net.cserny.videosmover.component;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import net.cserny.videosmover.model.*;
import net.cserny.videosmover.service.VideoMetadataService;
import net.cserny.videosmover.service.helper.SimpleVideoOutputHelper;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.CustomTextField;

import java.util.ArrayList;
import java.util.List;

// TODO: make prettier?
// TODO: make tooltip
// TODO: make it all a button
public class CustomTextFieldCell extends TableCell<VideoRow, String> {
    private final VideoMetadataService metadataService;
    private final CustomTextField customTextField;
    private final Button button;

    private StringProperty boundProperty = null;
    private SimpleVideoOutput videoOutput;

    public CustomTextFieldCell(VideoMetadataService metadataService) {
        this.metadataService = metadataService;
        this.button = initButton();
        this.customTextField = initCustomTextField();

        setGraphic(this.customTextField);
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
        customTextField.setStyle("-fx-text-fill: white");
        customTextField.setRight(button);
        return customTextField;
    }

    private Button initButton() {
        Image mainImage = new Image(getClass().getResourceAsStream("/images/scan-button.png"));
        Image altImage = new Image(getClass().getResourceAsStream("/images/tmdb_logo_small.png"));
        Image loadingImage = new Image(getClass().getResourceAsStream("/images/loading.gif"));
        ImageView imageView = buildButtonImageView(mainImage);

        Button button = new Button("", imageView);
        button.setVisible(false);
        button.setStyle("-fx-text-fill: WHITE; -fx-background-color: #01d277; -fx-cursor: hand;");
        button.setTooltip(new Tooltip("Some tooltip text"));
        button.setOnAction(event -> {
            setImageToButton(button, loadingImage);
            Runnable expensiveTask = () -> {
                List<VideoMetadata> videoMetadataList = processVideoMetadataList();
                setImageToButton(button, altImage);
                PopOver popOver = buildPopover(videoMetadataList, mainImage, altImage);
                Platform.runLater(() -> togglePopover(button, popOver));
            };
            new Thread(expensiveTask).start();
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
        vboxParent.setCursor(Cursor.HAND);
        for (VideoMetadata videoMetadata : videoMetadataList) {
            HBox hbox = new HBox();
            hbox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
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
        title.setStyle("-fx-font-weight: bold; -fx-fill: #081c24");

        String metadataDescription = videoMetadata.getDescription();
        if (metadataDescription.length() > 450) {
            metadataDescription = metadataDescription.substring(0, 450) + "...";
        }
        Text description = new Text(metadataDescription);
        description.setWrappingWidth(500);

        Text cast = new Text(String.join(", ", videoMetadata.getCast()));
        cast.setWrappingWidth(500);
        cast.setStyle("-fx-fill: #01d277");

        VBox vbox = new VBox();
        vbox.getChildren().addAll(title, description, cast);
        return vbox;
    }

    private void handleHboxClick(VideoMetadata videoMetadata, PopOver popOver) {
        customTextField.setText(SimpleVideoOutputHelper.formatOutput(videoOutput, videoMetadata));
        videoMetadata.setSelected(true);
        popOver.hide();
    }

    private List<VideoMetadata> processVideoMetadataList() {
        List<VideoMetadata> videoMetadataList = new ArrayList<>();
        VideoQuery videoQuery = VideoQuery.newInstance().withName(videoOutput.getName()).withYear(videoOutput.getYear()).build();
        if (videoOutput.getVideoType() == VideoType.MOVIE) {
            videoMetadataList = metadataService.searchMovieMetadata(videoQuery);
        } else if (videoOutput.getVideoType() == VideoType.TVSHOW) {
            videoMetadataList = metadataService.searchTvShowMetadata(videoQuery);
        }
        return videoMetadataList;
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
        }
        setContentDisplay(contentDisplay);
    }
}
