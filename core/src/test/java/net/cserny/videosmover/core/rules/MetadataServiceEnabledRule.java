package net.cserny.videosmover.core.rules;

import net.cserny.videosmover.core.service.CachedMetadataService;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadataServiceEnabledRule implements TestRule {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataServiceEnabledRule.class);
    private static final String CANNOT_EVALUATE = "Metadata service is not enabled, test cannot proceed, ignoring";

    private CachedMetadataService metadataService;

    public void setMetadataService(CachedMetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                if (metadataService != null && metadataService.isEnabled()) {
                    base.evaluate();
                } else {
                    LOGGER.warn(CANNOT_EVALUATE);
                }
            }
        };
    }
}
