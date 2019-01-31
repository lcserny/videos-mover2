package net.cserny.videosmover.web;

import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;

public class HtmlHttpExchange {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlHttpExchange.class);
    private static final String EXTENSION = ".html";
    private static final String PATH = "/templates/html";
    private static final String CONTENT_TYPE = "text/html";

    private final HttpExchange exchange;
    private final String context;

    public HtmlHttpExchange(HttpExchange exchange, String context) {
        this.exchange = exchange;
        this.context = context;
    }

    public void process() throws IOException {
        InputStream htmlStream = getClass().getResourceAsStream(PATH + context + EXTENSION);
        byte[] response = new BufferedInputStream(htmlStream).readAllBytes();

        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseHeaders().add("Content-Type", CONTENT_TYPE);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response);
        outputStream.close();
    }
}
