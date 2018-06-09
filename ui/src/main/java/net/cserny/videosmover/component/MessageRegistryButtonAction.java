package net.cserny.videosmover.component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import net.cserny.videosmover.service.SimpleMessageRegistry;

public class MessageRegistryButtonAction implements EventHandler<ActionEvent> {

    private final EventHandler<ActionEvent> eventHandler;
    private final SimpleMessageRegistry messageRegistry;

    public MessageRegistryButtonAction(EventHandler<ActionEvent> eventHandler, SimpleMessageRegistry messageRegistry) {
        this.eventHandler = eventHandler;
        this.messageRegistry = messageRegistry;
    }

    @Override
    public void handle(ActionEvent event) {
        eventHandler.handle(event);
        messageRegistry.displayMessages();
    }
}
