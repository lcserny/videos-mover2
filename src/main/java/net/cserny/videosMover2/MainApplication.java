package net.cserny.videosMover2;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.cserny.videosMover2.configure.ServiceInjector;
import net.cserny.videosMover2.controller.MainController;

/**
 * Created by leonardo on 02.09.2017.
 */
public class MainApplication extends Application
{
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Injector injector = Guice.createInjector(new ServiceInjector());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(loader.load());
        MainController controller = loader.getController();
        injector.injectMembers(controller);
        controller.setScene(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Downloads VideoMover");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/application.png")));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
