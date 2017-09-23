package net.cserny.videosmover.component;

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
import net.cserny.videosmover.service.PathsProvider;
import net.cserny.videosmover.service.VideoMetadataService;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.CustomTextField;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: change this to use regular JavaFX (two columns) cause ControlsFX is buggy?
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
        videoOutput = buildVideoOutput(output);
    }

    private void processForHide() {
        button.setVisible(false);
        videoOutput = null;
    }

    private CustomTextField initCustomTextField() {
        CustomTextField customTextField = new CustomTextField();
        customTextField.setStyle("-fx-text-fill: white");
        customTextField.setRight(button);
        customTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                processForHide();
            } else {
                processForShow(newValue);
            }
        });
        return customTextField;
    }

    private Button initButton() {
        Image mainImage = new Image(getClass().getResourceAsStream("/images/application.png"));
        Image altImage = new Image(getClass().getResourceAsStream("/images/scan-button.png"));

        ImageView imageView = new ImageView(mainImage);
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);

        Button button = new Button("", imageView);
        button.setVisible(false);
        button.setStyle("-fx-text-fill: WHITE; -fx-background-color: red; -fx-cursor: hand;");
        button.setTooltip(new Tooltip("Some tooltip text"));
        button.setOnAction(event -> {
            // TODO: show loading

            List<VideoMetadata> videoMetadataList = new ArrayList<>();
            VideoQuery videoQuery = VideoQuery.newInstance().withName(videoOutput.getName()).withYear(videoOutput.getYear()).build();
            if (videoOutput.getVideoType() == VideoType.MOVIE) {
                videoMetadataList = metadataService.searchMovieMetadata(videoQuery);
            } else if (videoOutput.getVideoType() == VideoType.TVSHOW) {
                videoMetadataList = metadataService.searchTvShowMetadata(videoQuery);
            }

            PopOver popOver = new PopOver();
            VBox vboxParent = new VBox();
            for (VideoMetadata videoMetadata : videoMetadataList) {
                HBox hbox = new HBox();
                hbox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                hbox.setOnMouseClicked(hboxEvent -> {
                    // FIXME: kind of a duplicate in VideoCacheRetriever
                    String formattedOutput = String.format("%s/%s (%s)", videoOutput.getPath(), videoMetadata.getName(), videoMetadata.getReleaseDate());
                    customTextField.setText(formattedOutput);
                    videoMetadata.setSelected(true);
                    popOver.hide();
                });
                hbox.setCursor(Cursor.HAND);

                hbox.getChildren().add(new ImageView(new Image(videoMetadata.getPosterUrl())));

                VBox vbox = new VBox();
                String metadataDescription = videoMetadata.getDescription();
                if (metadataDescription.length() > 500) {
                    metadataDescription = metadataDescription.substring(0, 500) + "...";
                }
                Text description = new Text(metadataDescription);
                description.setWrappingWidth(500);
                Text cast = new Text(String.join(", ", videoMetadata.getCast()));
                cast.setWrappingWidth(500);
                cast.setFill(Color.RED);
                vbox.getChildren().addAll(description, cast);
                hbox.getChildren().add(vbox);

                vboxParent.getChildren().add(hbox);
            }
            popOver.setContentNode(vboxParent);
            popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);
            popOver.setOnHidden(popEvent -> ((ImageView) button.getGraphic()).setImage(mainImage));
            popOver.setOnShowing(popEvent -> ((ImageView) button.getGraphic()).setImage(altImage));

            if (popOver.isShowing()) {
                popOver.hide();
            } else {
                popOver.show(button);
            }
        });

        return button;
    }

    // FIXME: duplicate in VideoCacheRetriever
    private SimpleVideoOutput buildVideoOutput(String output) {
        String path = output.substring(0, output.lastIndexOf('/'));
        String name = output.substring(output.lastIndexOf('/') + 1);
        Matcher matcher = Pattern.compile("(\\d{4})").matcher(name);
        Integer year = null;
        if (matcher.find()) {
            name = name.substring(0, matcher.start() - 2);
            year = Integer.valueOf(matcher.group());
        }

        VideoType videoType = null;
        if (path.equals(PathsProvider.getMoviesPath())) {
            videoType = VideoType.MOVIE;
        } else if (path.equals(PathsProvider.getTvShowsPath())) {
            videoType = VideoType.TVSHOW;
        }

        return new SimpleVideoOutput(name, year, path, videoType);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        ContentDisplay contentDisplay = ContentDisplay.TEXT_ONLY;
        if (!empty) {
            contentDisplay = ContentDisplay.GRAPHIC_ONLY;
            if (boundProperty == null) {
                boundProperty = (SimpleStringProperty) getTableColumn().getCellObservableValue(getIndex());
                customTextField.textProperty().bindBidirectional(boundProperty);
            }
        }
        setContentDisplay(contentDisplay);
    }
}