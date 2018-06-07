package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.validator.RemovalRestriction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VideoCleaner {

    private final Set<RemovalRestriction> removalRestrictions;
    private final SimpleMessageRegistry messageRegistry;

    @Autowired
    public VideoCleaner(Set<RemovalRestriction> removalRestrictions, SimpleMessageRegistry messageRegistry) {
        this.removalRestrictions = removalRestrictions;
        this.messageRegistry = messageRegistry;
    }

    public void clean(Video video) {
        for (RemovalRestriction restriction : removalRestrictions) {
            if (restriction.isRestricted(video)) {
                return;
            }
        }

        try {
            recursiveDelete(video.getInput().getParent());
        } catch (IOException e) {
            messageRegistry.add(MessageProvider.getCleanupFailed());
        }
    }

    public void clean(List<Video> videos) {
        for (Video video : videos) {
            clean(video);
        }
    }

    private void recursiveDelete(Path sourceParent) throws IOException {
        List<Path> sourceParentContentPaths = Files.walk(sourceParent).collect(Collectors.toList());
        for (Path path : sourceParentContentPaths) {
            if (path != sourceParent) {
                if (Files.isRegularFile(path)) {
                    Files.delete(path);
                } else if (Files.isDirectory(path)) {
                    recursiveDelete(path);
                }
            }
        }
        Files.deleteIfExists(sourceParent);
    }
}
