package net.cserny.videosmover.core.service;

import net.cserny.videosmover.core.model.Video;
import net.cserny.videosmover.core.service.parser.OutputVideoNameChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultOutputVideoNameService implements OutputVideoNameService {

    private List<OutputVideoNameChecker> outputVideoNameCheckers;

    @Autowired
    public DefaultOutputVideoNameService(List<OutputVideoNameChecker> outputVideoNameCheckers) {
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
