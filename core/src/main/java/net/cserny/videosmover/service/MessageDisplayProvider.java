package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Message;

public interface MessageDisplayProvider {
    void display(Message message);

    void init();
}
