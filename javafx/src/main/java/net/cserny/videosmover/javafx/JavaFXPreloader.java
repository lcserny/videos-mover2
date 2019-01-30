package net.cserny.videosmover.javafx;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JavaFXPreloader extends Preloader {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        Pane splashPane = new Pane(new ImageView(new Image(getClass().getResourceAsStream("/images/loading.gif"))));
        stage.setScene(new Scene(splashPane, Color.TRANSPARENT));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }
}
