package net.cserny.videosmover;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cserny.videosmover.controller.MainController;
import net.cserny.videosmover.error.GlobalExceptionCatcher;
import net.cserny.videosmover.provider.MainStageProvider;
import net.cserny.videosmover.DaggerMainComponent;
import net.cserny.videosmover.service.MessageDisplayProvider;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Set;

public class MainApplication extends Application {

    public static final String TITLE = "Downloads VideoMover";

    private Parent parent;

    @Inject
    GlobalExceptionCatcher exceptionCatcher;

    @Inject
    MainStageProvider stageProvider;

    @Inject
    MainController controller;

    @Inject
    Set<MessageDisplayProvider> messageDisplayProviders;

    @Override
    public void init() throws IOException {
        MainComponent component = DaggerMainComponent.create();
        component.inject(this);

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
        Thread.setDefaultUncaughtExceptionHandler(exceptionCatcher);
        messageDisplayProviders.forEach(MessageDisplayProvider::init);
    }
}
