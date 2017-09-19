package net.cserny.videosmover.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leonardo on 02.09.2017.
 */
@Service
public class SubtitlesFinderImpl implements SubtitlesFinder {
    @Value("#{'${video.subtitle.extensions}'.split(',')}")
    private List<String> subtitleExtensions;

    @Override
    public List<Path> find(Path file) throws IOException {
        Path directory = file.getParent();
        if (directory.toString().equals(PathsProvider.getDownloadsPath())) {
            return new ArrayList<>();
        }
        return collectSubtitles(directory);
    }

    private List<Path> collectSubtitles(Path directory) throws IOException {
        List<Path> subtitles = new ArrayList<>();
        List<Path> files = Files.walk(directory).filter(Files::isRegularFile).collect(Collectors.toList());
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
