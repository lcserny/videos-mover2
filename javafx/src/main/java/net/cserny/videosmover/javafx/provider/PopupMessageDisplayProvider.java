package net.cserny.videosmover.javafx.provider;

import javafx.scene.control.Alert;
import net.cserny.videosmover.core.model.Message;
import net.cserny.videosmover.core.service.MessageDisplayProvider;
import net.cserny.videosmover.core.service.SimpleMessageRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PopupMessageDisplayProvider implements MessageDisplayProvider {

    private final SimpleMessageRegistry messageRegistry;
    private final MainStageProvider stageProvider;

    @Autowired
    public PopupMessageDisplayProvider(SimpleMessageRegistry messageRegistry, MainStageProvider stageProvider) {
        this.stageProvider = stageProvider;
        this.messageRegistry = messageRegistry;
        this.messageRegistry.registerDisplayProvider(this);
    }

    @Override
    public void display(Message message) {
        Alert alert = new Alert(StaticMessageAlertTypeConverter.convert(message.getMessageType()), message.getContent());
        alert.setHeaderText(null);
        alert.setTitle(message.getTitle());
        alert.initOwner(stageProvider.getStage());
        alert.setOnHidden(event -> {
            messageRegistry.remove(message);
        });
        alert.show();
    }
}
