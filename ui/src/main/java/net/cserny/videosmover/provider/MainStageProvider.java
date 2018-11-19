package net.cserny.videosmover.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.stage.Stage;

@Singleton
public class MainStageProvider {

    private Stage stage;

    @Inject
    public MainStageProvider() { }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
