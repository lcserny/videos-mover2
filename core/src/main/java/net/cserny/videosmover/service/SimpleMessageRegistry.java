package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleMessageRegistry implements MessageRegistry {
    private List<Message> messages;
    private MessageDisplayProvider messageDisplayProvider;

    public SimpleMessageRegistry() {
        messages = new ArrayList<>();
    }

    @Override
    public void registerDisplayProvider(MessageDisplayProvider messageDisplayProvider) {
        this.messageDisplayProvider = messageDisplayProvider;
    }

    @Override
    public void displayMessages() {
        for (Message message : new ArrayList<>(messages)) {
            messageDisplayProvider.display(message);
        }
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void add(Message message) {
        messages.add(message);
    }

    @Override
    public void remove(Message message) {
        messages.remove(message);
    }
}
