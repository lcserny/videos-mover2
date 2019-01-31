package net.cserny.videosmover.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class TestHandler implements HttpHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestHandler.class);
    public static final String CONTEXT = "/test";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        LOGGER.info("Start of request for: " + CONTEXT);

        HtmlHttpExchange htmlHttpExchange = new HtmlHttpExchange(exchange, CONTEXT);
        htmlHttpExchange.process();

        LOGGER.info("End of request for: " + CONTEXT);
    }
}
