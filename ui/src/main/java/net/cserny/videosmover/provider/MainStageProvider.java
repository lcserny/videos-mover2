package net.cserny.videosmover.provider;

import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Singleton;

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
