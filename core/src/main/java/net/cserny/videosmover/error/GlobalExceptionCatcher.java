package net.cserny.videosmover.error;

import javafx.scene.control.Alert;
import net.cserny.videosmover.model.Message;
import net.cserny.videosmover.service.SimpleMessageRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GlobalExceptionCatcher implements Thread.UncaughtExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionCatcher.class);

    private final SimpleMessageRegistry messageRegistry;

    @Inject
    public GlobalExceptionCatcher(SimpleMessageRegistry messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        String content = e.toString();
        String title = e.getMessage() != null ? e.getMessage() : "Exception thrown";
        Message message = new Message(Alert.AlertType.ERROR, content, title);
        messageRegistry.add(message);
        messageRegistry.displayMessages();

        LOGGER.error(message.getTitle(), e);
    }
}
