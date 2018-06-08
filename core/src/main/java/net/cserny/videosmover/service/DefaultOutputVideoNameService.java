package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DefaultOutputVideoNameService implements OutputVideoNameService {

    @Autowired
    List<OutputVideoNameResolver> outputVideoNameResolvers;

    @Override
    public String resolve(Video video) throws IOException {
        for (OutputVideoNameResolver resolver : outputVideoNameResolvers) {
            if (resolver.canHandle(video.getVideoType())) {
                return resolver.resolve(video);
            }
        }

        throw new IOException(String.format("Couldn't resolve video name, no handler found, %s", video));
    }
}
