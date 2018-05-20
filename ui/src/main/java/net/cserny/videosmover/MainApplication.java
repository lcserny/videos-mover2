package net.cserny.videosmover;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
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

    @Override
    public void init() throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(MainApplication.class);
        builder.headless(false);
        context = builder.run();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setControllerFactory(context::getBean);
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

        context.getBean(MainStageProvider.class).setStage(primaryStage);
        Thread.setDefaultUncaughtExceptionHandler(context.getBean(GlobalExceptionCatcher.class));
    }

    @Override
    public void stop() {
        context.stop();
    }
}
