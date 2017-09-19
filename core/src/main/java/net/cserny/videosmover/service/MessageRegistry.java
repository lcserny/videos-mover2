package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Message;

import java.util.List;

public interface MessageRegistry {
    void registerDisplayProvider(MessageDisplayProvider messageDisplayProvider);

    void displayMessages();

    List<Message> getMessages();

    void add(Message message);

    void remove(Message message);
}
