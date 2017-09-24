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

// TODO: refactor this and make prettier
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

        ImageView imageView = new ImageView(mainImage);
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);

        Button button = new Button("", imageView);
        button.setVisible(false);
        button.setStyle("-fx-text-fill: WHITE; -fx-background-color: #01d277; -fx-cursor: hand;");
        button.setTooltip(new Tooltip("Some tooltip text"));
        button.setOnAction(event -> {
            ((ImageView) button.getGraphic()).setImage(loadingImage);

            Runnable expensiveTask = () -> {
                List<VideoMetadata> videoMetadataList = new ArrayList<>();
                VideoQuery videoQuery = VideoQuery.newInstance().withName(videoOutput.getName()).withYear(videoOutput.getYear()).build();
                if (videoOutput.getVideoType() == VideoType.MOVIE) {
                    videoMetadataList = metadataService.searchMovieMetadata(videoQuery);
                } else if (videoOutput.getVideoType() == VideoType.TVSHOW) {
                    videoMetadataList = metadataService.searchTvShowMetadata(videoQuery);
                }
                ((ImageView) button.getGraphic()).setImage(altImage);

                PopOver popOver = new PopOver();
                VBox vboxParent = new VBox();
                for (VideoMetadata videoMetadata : videoMetadataList) {
                    HBox hbox = new HBox();
                    hbox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                    hbox.setOnMouseClicked(hboxEvent -> {
                        customTextField.setText(SimpleVideoOutputHelper.formatOutput(videoOutput, videoMetadata));
                        videoMetadata.setSelected(true);
                        popOver.hide();
                    });
                    hbox.setCursor(Cursor.HAND);

                    hbox.getChildren().add(new ImageView(new Image(videoMetadata.getPosterUrl())));

                    VBox vbox = new VBox();
                    String metadataDescription = videoMetadata.getDescription();
                    if (metadataDescription.length() > 450) {
                        metadataDescription = metadataDescription.substring(0, 450) + "...";
                    }
                    Text title = new Text(videoMetadata.getName());
                    title.setStyle("-fx-font-weight: bold; -fx-fill: #081c24");
                    Text description = new Text(metadataDescription);
                    description.setWrappingWidth(500);
                    Text cast = new Text(String.join(", ", videoMetadata.getCast()));
                    cast.setWrappingWidth(500);
                    cast.setStyle("-fx-fill: #01d277");
                    vbox.getChildren().addAll(title, description, cast);
                    hbox.getChildren().add(vbox);

                    vboxParent.getChildren().add(hbox);
                }
                popOver.setContentNode(vboxParent);
                popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);
                popOver.setOnHidden(popEvent -> ((ImageView) button.getGraphic()).setImage(mainImage));
                popOver.setOnShowing(popEvent -> ((ImageView) button.getGraphic()).setImage(altImage));

                Platform.runLater(() -> {
                    if (popOver.isShowing()) {
                        popOver.hide();
                    } else {
                        popOver.show(button);
                    }
                });
            };
            new Thread(expensiveTask).start();
        });

        return button;
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
