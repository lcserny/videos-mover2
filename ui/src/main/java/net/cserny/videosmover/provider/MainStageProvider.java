package net.cserny.videosmover.provider;

import javafx.stage.Stage;

import javax.inject.Singleton;

@Singleton
public class MainStageProvider {

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
