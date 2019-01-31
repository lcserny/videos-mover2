package net.cserny.videosmover.web;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class WebApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebApplication.class);
    private static final int PORT = 9991;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        initContexts(server);
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();

        LOGGER.info("Started webserver at port " + PORT);
    }

    private static void initContexts(HttpServer server) {
        server.createContext(TestHandler.CONTEXT, new TestHandler());
        // TODO: add more
    }
}
