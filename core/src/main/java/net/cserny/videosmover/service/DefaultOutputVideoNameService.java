package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.parser.OutputVideoNameChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultOutputVideoNameService implements OutputVideoNameService {

    @Autowired
    List<OutputVideoNameChecker> outputVideoNameCheckers;

    @Override
    public void check(Video video) {
        for (OutputVideoNameChecker resolver : outputVideoNameCheckers) {
            if (resolver.canHandle(video.getVideoType())) {
                resolver.check(video);
            }
        }
    }
}
