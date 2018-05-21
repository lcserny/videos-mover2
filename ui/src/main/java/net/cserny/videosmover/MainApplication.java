package net.cserny.videosmover;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cserny.videosmover.DaggerMainComponent;
import net.cserny.videosmover.controller.MainController;
import net.cserny.videosmover.error.GlobalExceptionCatcher;
import net.cserny.videosmover.provider.MainStageProvider;
import net.cserny.videosmover.service.PathsInitializer;
import net.cserny.videosmover.service.StaticPathsProvider;

import javax.inject.Inject;

public class MainApplication extends Application {

    public static final String TITLE = "Downloads VideoMover";

    private MainComponent component;
    private Parent parent;

    @Inject
    GlobalExceptionCatcher exceptionCatcher;

    @Inject
    MainStageProvider stageProvider;

    @Inject
    MainController controller;

    @Override
    public void init() throws Exception {
        component = DaggerMainComponent.create();
        component.inject(this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setController(controller);
        parent = loader.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(parent));
        primaryStage.setTitle(TITLE);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/application.png")));
        primaryStage.centerOnScreen();
        primaryStage.show();

        stageProvider.setStage(primaryStage);
        Thread.setDefaultUncaughtExceptionHandler(exceptionCatcher);
    }
}
