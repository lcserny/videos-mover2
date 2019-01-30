package net.cserny.videosmover.javafx.component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class CallbackButtonAction implements EventHandler<ActionEvent> {

    private final EventHandler<ActionEvent> eventHandler;
    private final Runnable callback;

    public CallbackButtonAction(EventHandler<ActionEvent> eventHandler, Runnable callback) {
        this.eventHandler = eventHandler;
        this.callback = callback;
    }

    @Override
    public void handle(ActionEvent event) {
        eventHandler.handle(event);
        callback.run();
    }
}
