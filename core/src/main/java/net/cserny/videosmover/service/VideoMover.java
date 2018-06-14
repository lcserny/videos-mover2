package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Singleton
public class VideoMover {

    private static final String SUBTITLE_SUBPATH = "Subs";

    private final OutputVideoNameService videoNameService;

    @Inject
    public VideoMover(OutputVideoNameService videoNameService) {
        this.videoNameService = videoNameService;
    }

    public boolean move(Video video) throws IOException {
        videoNameService.check(video);

        Path sourceFile = video.getInputPath().resolve(video.getInputFilename());
        Path targetFolder = video.getOutputPath().resolve(video.getOutputFolderName());
        Path targetFile = targetFolder.resolve(video.getInputFilename());

        createDirectoryInternal(targetFolder);
        moveInternal(sourceFile, targetFile);

        List<Path> subtitles = video.getSubtitles();
        if (subtitles != null && !subtitles.isEmpty()) {
            for (Path subtitle : subtitles) {
                Path partialTarget = getPartialSubtitleTarget(targetFolder, subtitle);
                moveInternal(subtitle, partialTarget.resolve(subtitle.getFileName()));
            }
        }

        return Files.exists(targetFile);
    }

    public boolean move(List<Video> videoList) {
        for (Video video : videoList) {
            try {
                if (!move(video)) {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    private Path getPartialSubtitleTarget(Path target, Path subtitle) throws IOException {
        Path partialTarget = target;
        if (getParentFolderName(subtitle).equals(SUBTITLE_SUBPATH)) {
            partialTarget = target.resolve(SUBTITLE_SUBPATH);
            createDirectoryInternal(partialTarget);
        }
        return partialTarget;
    }

    private String getParentFolderName(Path path) {
        return path.getParent().getFileName().toString();
    }

    private void createDirectoryInternal(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            Files.createDirectory(directory);
        }
    }

    private void moveInternal(Path source, Path target) throws IOException {
        if (!Files.exists(target)) {
            Files.move(source, target);
        }
    }
}
