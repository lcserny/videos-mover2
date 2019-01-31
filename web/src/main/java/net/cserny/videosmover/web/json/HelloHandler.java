package net.cserny.videosmover.web.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloHandler extends JsonHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloHandler.class);
    public static final String CONTEXT = "/hello";

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected String getContext() {
        return CONTEXT;
    }
}
