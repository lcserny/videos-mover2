package net.cserny.videosmover;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cserny.videosmover.controller.MainController;
import net.cserny.videosmover.error.GlobalExceptionCatcher;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.provider.MainStageProvider;
import net.cserny.videosmover.service.MessageDisplayProvider;

import javax.inject.Inject;
import java.io.IOException;

public class MainApplication extends Application {

    public static final String TITLE = "Downloads VideoMover";

    @Inject
    GlobalExceptionCatcher exceptionCatcher;

    @Inject
    MainStageProvider stageProvider;

    @Inject
    MainController controller;

    @Inject
    MessageDisplayProvider messageDisplayProvider;

    private Parent parent;

    @Override
    public void init() throws IOException {
        initContext();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                StaticPathsProvider.getJoinedPathString("/fxml", "main.fxml")));
        loader.setController(controller);
        parent = loader.load();
    }

    private void initContext() {
        ApplicationComponent component = DaggerApplicationComponent.create();
        component.inject(this);

        Thread.setDefaultUncaughtExceptionHandler(exceptionCatcher);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(parent));
        primaryStage.setTitle(TITLE);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(
                StaticPathsProvider.getJoinedPathString("/images", "application.png"))));
        primaryStage.centerOnScreen();
        primaryStage.show();

        stageProvider.setStage(primaryStage);
    }
}
