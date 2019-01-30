package net.cserny.videosmover.core.service;

import net.cserny.videosmover.core.helper.StaticPathsProvider;
import net.cserny.videosmover.core.model.Subtitle;
import net.cserny.videosmover.core.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class VideoMover {

    private static final String SUBTITLE_SUBPATH = "Subs";

    private final OutputVideoNameService videoNameService;

    @Autowired
    public VideoMover(OutputVideoNameService videoNameService) {
        this.videoNameService = videoNameService;
    }

    public boolean move(Video video) throws IOException {
        videoNameService.check(video);

        Path sourceFile = StaticPathsProvider.getPath(video.getFullInputPath());
        Path targetFolder = generateTargetFolderPath(video);
        Path targetFile = targetFolder.resolve(video.getFileName());

        createDirectoryInternal(targetFolder);
        moveInternal(sourceFile, targetFile);

        List<Subtitle> subtitles = video.getSubtitles();
        if (subtitles != null && !subtitles.isEmpty()) {
            for (Subtitle subtitle : subtitles) {
                Path partialTarget = resolveSubtitleFolderPath(targetFolder, subtitle);
                moveInternal(StaticPathsProvider.getPath(subtitle.getFullInputPath()), partialTarget.resolve(subtitle.getFileName()));
            }
        }

        return Files.exists(targetFile);
    }

    private Path generateTargetFolderPath(Video video) {
        String rootPath = null;
        // TODO: make more OOP
        switch (video.getVideoType()) {
            case MOVIE:
                rootPath = StaticPathsProvider.getMoviesPath();
                break;
            case TVSHOW:
                rootPath = StaticPathsProvider.getTvShowsPath();
                break;
        }

        String pathString = StaticPathsProvider.joinPaths(rootPath, video.getOutputFolderWithDate());
        return StaticPathsProvider.getPath(pathString);
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

    private Path resolveSubtitleFolderPath(Path targetFolder, Subtitle subtitle) throws IOException {
        Path partialTarget = targetFolder;
        if (getParentFolderName(StaticPathsProvider.getPath(subtitle.getFullInputPath())).equals(SUBTITLE_SUBPATH)) {
            partialTarget = targetFolder.resolve(SUBTITLE_SUBPATH);
            createDirectoryInternal(partialTarget);
        }
        return partialTarget;
    }

    private String getParentFolderName(Path subtitlePath) {
        return subtitlePath.getParent().getFileName().toString();
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
