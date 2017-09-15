package net.cserny.videosMover.service;

import net.cserny.videosMover.model.Message;

import java.util.List;

public interface MessageRegistry
{
    void registerDisplayProvider(MessageDisplayProvider messageDisplayProvider);

    void displayMessages();

    List<Message> getMessages();

    void add(Message message);

    void remove(Message message);
}
