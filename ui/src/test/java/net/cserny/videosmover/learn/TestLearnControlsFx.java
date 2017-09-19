package net.cserny.videosmover.learn;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.CustomTextField;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javax.swing.text.TableView;

public class TestLearnControlsFx extends Application {
    private Pane root = new Pane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        addCustomTextField();

        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void addCustomTextField() {
        CustomTextField textField = new CustomTextField();

        Text text = new Text("Some text");
        textField.setLeft(text);

        Image image = new Image(getClass().getResourceAsStream("/images/application.png"));
        Image altImage = new Image(getClass().getResourceAsStream("/images/scan-button.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        Button button = new Button("", imageView);
        button.setStyle("-fx-text-fill: white; -fx-background-color: red; -fx-cursor: hand;");
        Tooltip tooltip = new Tooltip("Some tooltip text");
        button.setTooltip(tooltip);
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
        button.setOnAction(event -> {
            if (popOver.isShowing()) {
                popOver.hide();
                imageView.setImage(image);
            } else {
                popOver.show(button);
                imageView.setImage(altImage);
            }
        });
        textField.setRight(button);

        root.getChildren().addAll(textField);
    }
}
