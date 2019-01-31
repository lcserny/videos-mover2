package net.cserny.videosmover.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class TestHandler implements HttpHandler {

    public static final String CONTEXT = "/test";

    private static final Logger LOGGER = LoggerFactory.getLogger(TestHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        LOGGER.info("Start of request for: " + CONTEXT);

        byte[] response = "Check my RAM usage!".getBytes(Charset.forName("UTF-8"));
        exchange.sendResponseHeaders(200, response.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response);
        outputStream.close();

        LOGGER.info("End of request for: " + CONTEXT);
    }
}
