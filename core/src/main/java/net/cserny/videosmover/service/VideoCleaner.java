package net.cserny.videosmover.service;

import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.validator.RemovalRestriction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VideoCleaner {

    private List<RemovalRestriction> removalRestrictions;
    private final SimpleMessageRegistry messageRegistry;

    @Autowired
    public VideoCleaner(List<RemovalRestriction> removalRestrictions, SimpleMessageRegistry messageRegistry) {
        this.removalRestrictions = removalRestrictions;
        this.messageRegistry = messageRegistry;
    }

    public void clean(Path inputFolderPath) {
        for (RemovalRestriction restriction : removalRestrictions) {
            if (restriction.isRestricted(inputFolderPath)) {
                messageRegistry.displayMessage(MessageProvider.removalNotAllowed(inputFolderPath.toString()));
                return;
            }
        }

        try {
            recursiveDelete(inputFolderPath);
        } catch (IOException e) {
            messageRegistry.displayMessage(MessageProvider.cleanupFailed());
        }
    }

    public void clean(List<Video> videos) {
        videos.stream().map(video -> StaticPathsProvider.getPath(video.getInputPathWithoutFileName()))
                .collect(Collectors.toSet())
                .forEach(this::clean);
    }

    private void recursiveDelete(Path inputFolderPath) throws IOException {
        List<Path> sourceParentContentPaths = Files.walk(inputFolderPath, Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)
                .collect(Collectors.toList());
        for (Path path : sourceParentContentPaths) {
            if (path != inputFolderPath) {
                if (Files.isRegularFile(path)) {
                    Files.delete(path);
                } else if (Files.isDirectory(path)) {
                    recursiveDelete(path);
                }
            }
        }
        Files.deleteIfExists(inputFolderPath);
    }
}
