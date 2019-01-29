package net.cserny.videosmover.service;

import net.cserny.videosmover.helper.PropertiesLoader;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Subtitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubtitlesFinder {

    private List<String> subtitleExtensions;

    @Autowired
    public SubtitlesFinder() {
        subtitleExtensions = PropertiesLoader.getSubtitleExtensions();
    }

    public List<Subtitle> find(Path directory) {
        if (directory.toString().equals(StaticPathsProvider.getDownloadsPath())) {
            return Collections.emptyList();
        }
        return collectSubtitles(directory);
    }

    private List<Subtitle> collectSubtitles(Path videoDirectory) {
        try {
            return Files.walk(videoDirectory, 2, FileVisitOption.FOLLOW_LINKS)
                    .filter(Files::isRegularFile)
                    .filter(this::filterSubtitles)
                    .map(subtitlePath -> mapSubtitle(subtitlePath, videoDirectory))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private Subtitle mapSubtitle(Path subtitlePath, Path videoDirectory) {
        Subtitle subtitle = new Subtitle(subtitlePath.getFileName().toString(), subtitlePath.toString());
        if (!videoDirectory.equals(subtitlePath.getParent())) {
            subtitle.setSubFolder(subtitlePath.getParent().getFileName().toString());
        }
        return subtitle;
    }

    private boolean filterSubtitles(Path subtitle) {
        for (String subtitleExtension : subtitleExtensions) {
            if (subtitle.getFileName().toString().endsWith(subtitleExtension)) {
                return true;
            }
        }
        return false;
    }
}
