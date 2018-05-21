package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Message;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class SimpleMessageRegistry {

    private List<Message> messages;
    private MessageDisplayProvider messageDisplayProvider;

    public SimpleMessageRegistry() {
        messages = new ArrayList<>();
    }

    public void registerDisplayProvider(MessageDisplayProvider messageDisplayProvider) {
        this.messageDisplayProvider = messageDisplayProvider;
    }

    public void displayMessages() {
        for (Message message : new ArrayList<>(messages)) {
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
