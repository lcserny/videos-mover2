package net.cserny.videosmover.javafx.provider;

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
import net.cserny.videosmover.core.model.Message;
import net.cserny.videosmover.core.service.MessageDisplayProvider;
import net.cserny.videosmover.core.service.SimpleMessageRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InWindowMessageDisplayProvider implements MessageDisplayProvider {

    private final SimpleMessageRegistry messageRegistry;
    private final MainStageProvider stageProvider;

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
        Color flashColor = StaticMessageAlertTypeConverter.convert(message.getMessageType()) == Alert.AlertType.ERROR ? Color.RED : Color.BLUE;
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
