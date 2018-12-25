package net.cserny.videosmover.error;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.Alert;
import net.cserny.videosmover.helper.LoadingService;
import net.cserny.videosmover.model.Message;
import net.cserny.videosmover.service.SimpleMessageRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class GlobalExceptionCatcher implements Thread.UncaughtExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionCatcher.class);

    private final SimpleMessageRegistry messageRegistry;
    private final LoadingService loadingService;

    @Inject
    public GlobalExceptionCatcher(SimpleMessageRegistry messageRegistry, LoadingService loadingService) {
        this.messageRegistry = messageRegistry;
        this.loadingService = loadingService;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        String content = e.toString();
        String title = e.getMessage() != null ? e.getMessage() : "Exception thrown";
        Message message = new Message(Alert.AlertType.ERROR, content, title);
        messageRegistry.displayMessage(message);
        loadingService.hideAllLoadings();

        LOGGER.error(message.getTitle(), e);
    }
}
