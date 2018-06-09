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
import net.cserny.videosmover.service.PathsInitializer;
import net.cserny.videosmover.service.StaticPathsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApplication extends Application {

    public static final String TITLE = "Downloads VideoMover";

    private MainStageProvider stageProvider;
    private Parent parent;
    private AnnotationConfigApplicationContext context;

    @Override
    public void init() throws Exception {
        initContext();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setControllerFactory(context::getBean);
        parent = loader.load();
    }

    private void initContext() {
        context = new AnnotationConfigApplicationContext("net.cserny.videosmover");
        stageProvider = context.getBean(MainStageProvider.class);
        Thread.setDefaultUncaughtExceptionHandler(context.getBean(GlobalExceptionCatcher.class));
        StaticPathsProvider.pathsInitializer = context.getBean(PathsInitializer.class);
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
    }

    @Override
    public void stop() {
        context.stop();
    }
}
