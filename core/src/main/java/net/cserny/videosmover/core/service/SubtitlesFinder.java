package net.cserny.videosmover.core.service;

import net.cserny.videosmover.core.helper.StaticPathsProvider;
import net.cserny.videosmover.core.model.Subtitle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static net.cserny.videosmover.core.constants.PropertyConstants.SUBTITLE_EXTENSIONS_KEY;

@Service
public class SubtitlesFinder {

    @Value("#{'${" + SUBTITLE_EXTENSIONS_KEY + "}'.split(',')}")
    private List<String> subtitleExtensions;

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
