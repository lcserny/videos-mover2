package net.cserny.videosmover.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cserny.videosmover.core.CoreConfiguration;
import net.cserny.videosmover.javafx.controller.MainController;
import net.cserny.videosmover.core.error.GlobalExceptionCatcher;
import net.cserny.videosmover.javafx.provider.MainStageProvider;
import net.cserny.videosmover.core.service.thread.TwoThreadsExecutor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class JavaFXApplication extends Application {

    private static final String TITLE = "Downloads VideoMover";

    private ConfigurableApplicationContext context;
    private Parent parent;

    @Override
    public void init() throws IOException {
        context = new AnnotationConfigApplicationContext(JavaFXConfiguration.class, CoreConfiguration.class);
        Thread.setDefaultUncaughtExceptionHandler(context.getBean(GlobalExceptionCatcher.class));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setControllerFactory(context::getBean);
        parent = loader.load();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(parent));
        primaryStage.setTitle(TITLE);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/application.png")));
        primaryStage.centerOnScreen();
        primaryStage.show();

        context.getBean(MainStageProvider.class).setStage(primaryStage);
        context.getBean(MainController.class).initLoading();
    }

    @Override
    public void stop() throws Exception {
        context.stop();
        TwoThreadsExecutor.shutdown();
    }
}
