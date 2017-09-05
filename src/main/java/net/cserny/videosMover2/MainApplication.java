package net.cserny.videosMover2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cserny.videosMover2.configuration.ControllerConfig;
import net.cserny.videosMover2.configuration.ServiceConfig;
import net.cserny.videosMover2.controller.MainController;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by leonardo on 02.09.2017.
 */
public class MainApplication extends Application
{
    private ConfigurableApplicationContext context;
    private FXMLLoader loader;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
        context = new AnnotationConfigApplicationContext(ControllerConfig.class, ServiceConfig.class);
        loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setControllerFactory(context::getBean);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(loader.load());
        MainController controller = loader.getController();
        controller.setScene(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Downloads VideoMover");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/application.png")));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        context.stop();
    }
}
