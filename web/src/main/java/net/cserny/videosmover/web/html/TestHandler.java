package net.cserny.videosmover.web.html;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestHandler extends HtmlHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestHandler.class);
    public static final String CONTEXT = "/test";

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected String getContext() {
        return CONTEXT;
    }
}
