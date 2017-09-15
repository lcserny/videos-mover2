package net.cserny.videosMover;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cserny.videosMover.provider.MainStageProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by leonardo on 02.09.2017.
 */
@Configuration
@ComponentScan
public class MainApplication extends Application
{
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setControllerFactory(context::getBean);

        MainStageProvider stageProvider = context.getBean(MainStageProvider.class);
        stageProvider.setStage(primaryStage);

        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Downloads VideoMover");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/application.png")));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
