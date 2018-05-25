package net.cserny.videosmover.provider;

import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class MainStageProvider {

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
