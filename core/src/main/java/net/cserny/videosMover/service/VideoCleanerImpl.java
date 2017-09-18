package net.cserny.videosMover.service;

import net.cserny.videosMover.model.Video;
import net.cserny.videosMover.service.validator.RemovalRestriction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoCleanerImpl implements VideoCleaner {
    private final List<RemovalRestriction> removalRestrictions;
    private final MessageRegistry messageRegistry;

    @Autowired
    public VideoCleanerImpl(List<RemovalRestriction> removalRestrictions, MessageRegistry messageRegistry) {
        this.removalRestrictions = removalRestrictions;
        this.messageRegistry = messageRegistry;
    }

    @Override
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

    @Override
    public void cleanAll(List<Video> videos) {
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
