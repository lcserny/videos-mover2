package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.parser.OutputVideoNameChecker;

import javax.inject.Inject;
import java.util.Set;

public class DefaultOutputVideoNameService implements OutputVideoNameService {

    private Set<OutputVideoNameChecker> outputVideoNameCheckers;

    @Inject
    public DefaultOutputVideoNameService(Set<OutputVideoNameChecker> outputVideoNameCheckers) {
        this.outputVideoNameCheckers = outputVideoNameCheckers;
    }

    @Override
    public void check(Video video) {
        for (OutputVideoNameChecker resolver : outputVideoNameCheckers) {
            if (resolver.canHandle(video.getVideoType())) {
                resolver.check(video);
            }
        }
    }
}
