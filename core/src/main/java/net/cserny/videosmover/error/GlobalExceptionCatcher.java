package net.cserny.videosmover.error;

import javafx.scene.control.Alert;
import net.cserny.videosmover.model.Message;
import net.cserny.videosmover.service.MessageRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GlobalExceptionCatcher implements Thread.UncaughtExceptionHandler {
    private final MessageRegistry messageRegistry;

    @Autowired
    public GlobalExceptionCatcher(MessageRegistry messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        String content = e.toString();
        String title = e.getMessage() != null ? e.getMessage() : "Exception thrown";
        messageRegistry.add(new Message(Alert.AlertType.ERROR, content, title));
        messageRegistry.displayMessages();
    }
}
