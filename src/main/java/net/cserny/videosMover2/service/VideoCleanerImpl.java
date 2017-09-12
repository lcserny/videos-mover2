package net.cserny.videosMover2.service;

import net.cserny.videosMover2.dto.Video;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class VideoCleanerImpl implements VideoCleaner
{
    @Override
    public void clean(Video video) throws IOException {
        Path sourceParent = video.getInput().getParent();
        if (!sourceParent.toString().equals(PathsProvider.getDownloadsPath())) {
            recursiveDelete(sourceParent);
        }
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
