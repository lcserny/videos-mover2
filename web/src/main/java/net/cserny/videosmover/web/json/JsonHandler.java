package net.cserny.videosmover.web.json;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.cserny.videosmover.web.HttpExchangeProcessor;
import net.cserny.videosmover.web.ResourceExchangeProcessor;
import org.slf4j.Logger;

import java.io.IOException;

public abstract class JsonHandler implements HttpHandler {

    private static final String EXTENSION = ".json";
    private static final String PATH = "/templates/json";
    private static final String CONTENT_TYPE = "application/json";

    protected abstract Logger getLogger();

    protected abstract String getContext();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        getLogger().info("Start of request for: " + getContext());

        HttpExchangeProcessor htmlHttpExchange = new ResourceExchangeProcessor(exchange, PATH, EXTENSION, getContext(), CONTENT_TYPE);
        htmlHttpExchange.process();

        getLogger().info("End of request for: " + getContext());
    }
}
