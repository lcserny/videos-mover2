package net.cserny.videosmover.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;

import java.io.IOException;

public abstract class HtmlHandler implements HttpHandler {

    protected abstract Logger getLogger();

    protected abstract String getContext();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        getLogger().info("Start of request for: " + getContext());

        HttpExchangeProcessor htmlHttpExchange = new HtmlHttpExchangeProcessor(exchange, getContext());
        htmlHttpExchange.process();

        getLogger().info("End of request for: " + getContext());
    }
}
