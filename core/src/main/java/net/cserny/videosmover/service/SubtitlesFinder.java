package net.cserny.videosmover.service;

import net.cserny.videosmover.PropertiesLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SubtitlesFinder {

    private List<String> subtitleExtensions;

    public SubtitlesFinder() {
        subtitleExtensions = PropertiesLoader.getSubtitleExtensions();
    }

    public List<Path> find(Path file) throws IOException {
        Path directory = file.getParent();
        if (directory.toString().equals(StaticPathsProvider.getDownloadsPath())) {
            return Collections.emptyList();
        }
        return collectSubtitles(directory);
    }

    private List<Path> collectSubtitles(Path directory) throws IOException {
        List<Path> files = Files.walk(directory).filter(Files::isRegularFile).collect(Collectors.toList());
        List<Path> subtitles = new ArrayList<>();
        for (Path tmpFile : files) {
            for (String subtitleExtension : subtitleExtensions) {
                if (tmpFile.toString().endsWith(subtitleExtension)) {
                    subtitles.add(tmpFile);
                    break;
                }
            }
        }
        return subtitles;
    }
}
