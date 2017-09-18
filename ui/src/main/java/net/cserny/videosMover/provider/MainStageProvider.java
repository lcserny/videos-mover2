package net.cserny.videosMover.provider;

import javafx.stage.Stage;
import org.springframework.stereotype.Service;

@Service
public class MainStageProvider {
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
