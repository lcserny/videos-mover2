package net.cserny.videosmover.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import net.cserny.videosmover.model.VideoRow;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.CustomTextField;

public class CustomTextFieldCell extends TableCell<VideoRow, String> {
    private final CustomTextField customTextField;
    private final Button button;
    private final Image mainImage;
    private final Image altImage;
    private final PopOver popOver;
    private StringProperty boundProperty = null;

    public CustomTextFieldCell() {
        this.mainImage = new Image(getClass().getResourceAsStream("/images/application.png"));
        this.altImage = new Image(getClass().getResourceAsStream("/images/scan-button.png"));

        this.button = initButton();
        this.popOver = initPopOver();
        this.customTextField = initCustomTextField();

        setGraphic(this.customTextField);
    }

    public void changeColors() {
        button.setStyle("-fx-text-fill: white; -fx-background-color: blue; -fx-cursor: hand;");
    }

    private CustomTextField initCustomTextField() {
        CustomTextField customTextField = new CustomTextField();
        customTextField.setStyle("-fx-text-fill: white");
        customTextField.setRight(this.button);
        return customTextField;
    }

    private PopOver initPopOver() {
        VBox vBox = new VBox();
        Button one = new Button("one");
        one.setPrefWidth(100);
        Button two = new Button("two");
        two.setPrefWidth(100);
        Button three = new Button("three");
        three.setPrefWidth(100);
        Button four = new Button("four");
        four.setPrefWidth(100);
        vBox.getChildren().addAll(one, two, three, four);

        PopOver popOver = new PopOver(vBox);
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popOver.setOnHidden(event -> ((ImageView) button.getGraphic()).setImage(mainImage));

        return popOver;
    }

    private Button initButton() {
        ImageView imageView = new ImageView(mainImage);
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);

        Button button = new Button("", imageView);
        button.setStyle("-fx-text-fill: white; -fx-background-color: red; -fx-cursor: hand;");
        button.setTooltip(new Tooltip("Some tooltip text"));
        button.setOnAction(event -> {
            if (popOver.isShowing()) {
                popOver.hide();
            } else {
                popOver.show(button);
                ((ImageView) button.getGraphic()).setImage(altImage);
            }
        });

        return button;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty) {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            if (boundProperty == null) {
                boundProperty = (SimpleStringProperty) getTableColumn().getCellObservableValue(getIndex());
                customTextField.textProperty().bindBidirectional(boundProperty);
            }
        } else {
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }
}
