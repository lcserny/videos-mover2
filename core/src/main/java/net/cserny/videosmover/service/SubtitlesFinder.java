package net.cserny.videosmover.service;

import net.cserny.videosmover.helper.PropertiesLoader;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Subtitle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class SubtitlesFinder {

    private List<String> subtitleExtensions;

    @Inject
    public SubtitlesFinder() {
        subtitleExtensions = PropertiesLoader.getSubtitleExtensions();
    }

    public List<Subtitle> find(Path videoPath) {
        Path directory = videoPath.getParent();
        if (directory.toString().equals(StaticPathsProvider.getDownloadsPath())) {
            return Collections.emptyList();
        }
        return collectSubtitles(directory);
    }

    private List<Subtitle> collectSubtitles(Path directory) {
        try {
            return Files.walk(directory, 2, FileVisitOption.FOLLOW_LINKS)
                    .filter(Files::isRegularFile)
                    .filter(this::filterSubtitles)
                    .map(subtitlePath -> mapSubtitle(subtitlePath, directory))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private Subtitle mapSubtitle(Path subtitlePath, Path videoInputPath) {
        Subtitle subtitle = new Subtitle();
        subtitle.setFileName(subtitlePath.getFileName().toString());

        subtitle.setInputPath(subtitlePath.getParent().toString());
        if (!videoInputPath.equals(subtitlePath.getParent())) {
            subtitle.setInputFolderName(subtitlePath.getParent().getFileName().toString());
        }
        return subtitle;
    }

    private boolean filterSubtitles(Path subtitle) {
        for (String subtitleExtension : subtitleExtensions) {
            if (subtitle.toString().endsWith(subtitleExtension)) {
                return true;
            }
        }
        return false;
    }
}
