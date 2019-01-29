package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleMessageRegistry {

    private List<Message> messages = new ArrayList<>();
    private MessageDisplayProvider messageDisplayProvider;

    @Autowired
    public SimpleMessageRegistry() {
    }

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
