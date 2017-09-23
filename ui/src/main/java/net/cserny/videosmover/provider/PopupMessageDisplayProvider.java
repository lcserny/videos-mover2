package net.cserny.videosmover.provider;

import javafx.scene.control.Alert;
import net.cserny.videosmover.model.Message;
import net.cserny.videosmover.service.MessageDisplayProvider;
import net.cserny.videosmover.service.MessageRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PopupMessageDisplayProvider implements MessageDisplayProvider {
    private final MessageRegistry messageRegistry;
    private final MainStageProvider stageProvider;

    @Autowired
    public PopupMessageDisplayProvider(MessageRegistry messageRegistry, MainStageProvider stageProvider) {
        this.stageProvider = stageProvider;
        this.messageRegistry = messageRegistry;
        this.messageRegistry.registerDisplayProvider(this);
    }

    @Override
    public void display(Message message) {
        Alert alert = new Alert(message.getAlertType(), message.getContent());
        alert.setHeaderText(null);
        alert.setTitle(message.getTitle());
        alert.initOwner(stageProvider.getStage());
        alert.setOnHidden(event -> { messageRegistry.remove(message); });
        alert.show();
    }
}