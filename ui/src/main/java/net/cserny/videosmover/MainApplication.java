package net.cserny.videosmover;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.cserny.videosmover.provider.MainStageProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by leonardo on 02.09.2017.
 */
public class MainApplication extends Application {
    public static final String TITLE = "Downloads VideoMover";
    public static final int LOADING_SIZE = 120;
    public static final int WINDOW_WIDTH = 900;
    public static final int WINDOW_HEIGHT = 650;

    private Stage mainStage;
    private Parent parent;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Task<Void> loadSpringContextTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                loader.setControllerFactory(context::getBean);
                context.getBean(MainStageProvider.class).setStage(mainStage);
                parent = loader.load();

                return null;
            }
        };

        showLoading(primaryStage, loadSpringContextTask);
        new Thread(loadSpringContextTask).start();
    }

    private void showLoading(final Stage initStage, Task<?> task) {
        Pane splashPane = new Pane(new ImageView(new Image(getClass().getResourceAsStream("/images/loading.gif"))));
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1), splashPane);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                showMainStage();
            }
        });

        initStage.setScene(new Scene(splashPane, Color.TRANSPARENT));
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.setAlwaysOnTop(true);
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - LOADING_SIZE / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - LOADING_SIZE / 2);
        initStage.show();
    }

    private void showMainStage() {
        mainStage = new Stage();
        mainStage.setScene(new Scene(parent));
        mainStage.setTitle(TITLE);
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/application.png")));
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        mainStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - WINDOW_WIDTH / 2);
        mainStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - WINDOW_HEIGHT / 2);
        mainStage.show();
    }
}
