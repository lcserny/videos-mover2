package net.cserny.videosmover;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.cserny.videosmover.error.GlobalExceptionCatcher;
import net.cserny.videosmover.provider.MainStageProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by leonardo on 02.09.2017.
 */
@SpringBootApplication
public class MainApplication extends Application {
    public static final String TITLE = "Downloads VideoMover";

    private ConfigurableApplicationContext context;
    private Parent parent;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Task<Void> loadSpringContextTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                SpringApplicationBuilder builder = new SpringApplicationBuilder(MainApplication.class);
                builder.headless(false);
                context = builder.run();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                loader.setControllerFactory(context::getBean);
                parent = loader.load();

                Thread.setDefaultUncaughtExceptionHandler(context.getBean(GlobalExceptionCatcher.class));
                return null;
            }
        };

        showLoading(primaryStage, loadSpringContextTask);
        new Thread(loadSpringContextTask).start();
    }

    @Override
    public void stop() throws Exception {
        context.stop();
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
        initStage.centerOnScreen();
        initStage.show();
    }

    private void showMainStage() {
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(parent));
        mainStage.setTitle(TITLE);
        mainStage.setResizable(false);
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/application.png")));
        mainStage.centerOnScreen();
        mainStage.show();

        context.getBean(MainStageProvider.class).setStage(mainStage);
    }
}
