package net.cserny.videosmover.service;

import net.cserny.videosmover.helper.PropertiesLoader;
import net.cserny.videosmover.helper.StaticPathsProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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

    public List<Path> find(Path file) {
        Path directory = file.getParent();
        if (directory.toString().equals(StaticPathsProvider.getDownloadsPath())) {
            return Collections.emptyList();
        }
        return collectSubtitles(directory);
    }

    private List<Path> collectSubtitles(Path directory) {
        try {
            return Files.walk(directory, Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)
                    .filter(Files::isRegularFile)
                    .filter(this::filterSubtitles)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private boolean filterSubtitles(Path subtitle) {
        boolean ok = false;
        for (String subtitleExtension : subtitleExtensions) {
            if (subtitle.toString().endsWith(subtitleExtension)) {
                ok = true;
                break;
            }
        }
        return ok;
    }
}
