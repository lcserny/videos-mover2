package net.cserny.videosMover.service;

import javafx.scene.control.Alert;
import net.cserny.videosMover.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PopupMessageDisplayProvider implements MessageDisplayProvider
{
    private MessageRegistry messageRegistry;

    @Autowired
    public PopupMessageDisplayProvider(MessageRegistry messageRegistry) {
        this.messageRegistry = messageRegistry;
        this.messageRegistry.registerDisplayProvider(this);
    }

    @Override
    public void display(Message message) {
        Alert alert = new Alert(message.getAlertType(), message.getContent());
        alert.setHeaderText(null);
        alert.setTitle(message.getTitle());
        alert.setOnHidden(event -> { messageRegistry.remove(message); });
        alert.show();
    }
}
