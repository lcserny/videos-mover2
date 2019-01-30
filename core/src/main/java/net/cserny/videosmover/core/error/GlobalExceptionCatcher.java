package net.cserny.videosmover.core.error;

import net.cserny.videosmover.core.constants.MessageType;
import net.cserny.videosmover.core.helper.LoadingService;
import net.cserny.videosmover.core.model.Message;
import net.cserny.videosmover.core.service.SimpleMessageRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalExceptionCatcher implements Thread.UncaughtExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionCatcher.class);

    private final SimpleMessageRegistry messageRegistry;
    private final LoadingService loadingService;

    @Autowired
    public GlobalExceptionCatcher(SimpleMessageRegistry messageRegistry, LoadingService loadingService) {
        this.messageRegistry = messageRegistry;
        this.loadingService = loadingService;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        String content = e.toString();
        String title = e.getMessage() != null ? e.getMessage() : "Exception thrown";
        Message message = new Message(MessageType.ERROR, content, title);
        messageRegistry.displayMessage(message);
        loadingService.hideAllLoadings();

        LOGGER.error(message.getTitle(), e);
    }
}
