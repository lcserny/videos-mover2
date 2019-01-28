package net.cserny.videosmover;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cserny.videosmover.controller.MainController;
import net.cserny.videosmover.error.GlobalExceptionCatcher;
import net.cserny.videosmover.provider.MainStageProvider;
import net.cserny.videosmover.service.MessageDisplayProvider;
import net.cserny.videosmover.service.thread.TwoThreadsExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class MainApplication extends Application {

    public static final String TITLE = "Downloads VideoMover";

    @Autowired
    GlobalExceptionCatcher exceptionCatcher;

    @Autowired
    MainStageProvider stageProvider;

    @Autowired
    MainController controller;

    @Autowired
    MessageDisplayProvider messageDisplayProvider;

    private ConfigurableApplicationContext context;
    private Parent parent;

    @Override
    public void init() throws IOException {
        context = SpringApplication.run(MainApplication.class);
        Thread.setDefaultUncaughtExceptionHandler(exceptionCatcher);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setController(controller);
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

        stageProvider.setStage(primaryStage);
        controller.initLoading();
    }

    @Override
    public void stop() throws Exception {
        context.stop();
        TwoThreadsExecutor.shutdown();
    }
}
