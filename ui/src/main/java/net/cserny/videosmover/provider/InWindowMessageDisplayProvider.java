package net.cserny.videosmover.provider;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import net.cserny.videosmover.model.Message;
import net.cserny.videosmover.service.MessageDisplayProvider;
import net.cserny.videosmover.service.SimpleMessageRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class InWindowMessageDisplayProvider implements MessageDisplayProvider {

    private final SimpleMessageRegistry messageRegistry;
    private MainStageProvider stageProvider;

    @Autowired
    public InWindowMessageDisplayProvider(SimpleMessageRegistry messageRegistry, MainStageProvider stageProvider) {
        this.messageRegistry = messageRegistry;
        this.stageProvider = stageProvider;
        this.messageRegistry.registerDisplayProvider(this);
    }

    @Override
    public void display(Message message) {
        TextArea messageTextArea = appendText(message);
        flash(messageTextArea, message);
        messageRegistry.remove(message);
    }

    private TextArea appendText(Message message) {
        TextArea messageTextArea = (TextArea) stageProvider.getStage().getScene().lookup("#messageText");
        if (!messageTextArea.getText().trim().isEmpty()) {
            messageTextArea.appendText("\n");
        }
        messageTextArea.appendText(message.getContent());
        return messageTextArea;
    }

    private void flash(TextArea messageTextArea, Message message) {
        Color flashColor = message.getAlertType() == Alert.AlertType.ERROR ? Color.RED : Color.BLUE;
        Background background = messageTextArea.getBackground();
        Timeline flasher = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    messageTextArea.setBackground(new Background(new BackgroundFill(flashColor, CornerRadii.EMPTY, Insets.EMPTY)));
                }),
                new KeyFrame(Duration.seconds(1), event -> {
                    messageTextArea.setBackground(background);
                })
        );
        flasher.setCycleCount(2);
        flasher.play();
    }
}
