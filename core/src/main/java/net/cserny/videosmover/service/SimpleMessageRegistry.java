package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Message;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class SimpleMessageRegistry {

    private List<Message> messages = new ArrayList<>();
    private MessageDisplayProvider messageDisplayProvider;

    @Inject
    public SimpleMessageRegistry() { }

    public void registerDisplayProvider(MessageDisplayProvider messageDisplayProvider) {
        this.messageDisplayProvider = messageDisplayProvider;
    }

    public void displayMessage(Message message) {
        if (messageDisplayProvider != null) {
            messageDisplayProvider.display(message);
        }
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void add(Message message) {
        messages.add(message);
    }

    public void remove(Message message) {
        messages.remove(message);
    }
}
