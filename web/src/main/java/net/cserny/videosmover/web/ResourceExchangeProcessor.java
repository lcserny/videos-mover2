package net.cserny.videosmover.web;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ResourceExchangeProcessor implements HttpExchangeProcessor {

    private HttpExchange exchange;
    private String path;
    private String extension;
    private String context;
    private String contentType;

    public ResourceExchangeProcessor(HttpExchange exchange, String path, String extension, String context, String contentType) {
        this.exchange = exchange;
        this.path = path;
        this.extension = extension;
        this.context = context;
        this.contentType = contentType;
    }

    @Override
    public void process() throws IOException {
        InputStream htmlStream = getClass().getResourceAsStream(path + context + extension);
        byte[] response = new BufferedInputStream(htmlStream).readAllBytes();

        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseHeaders().add("Content-Type", contentType);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response);
        outputStream.flush();
    }
}
