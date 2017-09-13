package net.cserny.videosMover2.service;

import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.service.validator.RemovalRestriction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoCleanerImpl implements VideoCleaner
{
    private List<RemovalRestriction> removalRestrictions;

    @Autowired
    public VideoCleanerImpl(List<RemovalRestriction> removalRestrictions) {
        this.removalRestrictions = removalRestrictions;
    }

    @Override
    public void clean(Video video) throws IOException {
        for (RemovalRestriction restriction : removalRestrictions) {
            if (restriction.isRestricted(video)) {
                return;
            }
        }
        recursiveDelete(video.getInput().getParent());
    }

    @Override
    public void cleanAll(List<Video> videos) throws IOException {
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
