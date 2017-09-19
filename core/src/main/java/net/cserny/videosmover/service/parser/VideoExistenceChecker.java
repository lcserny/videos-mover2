package net.cserny.videosmover.service.parser;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.cserny.videosmover.service.PathsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by leonardo on 10.09.2017.
 */
@Service
@Order(2)
public class VideoExistenceChecker implements VideoNameParser {
    @Value("${similarity.percent}")
    private Integer similarityPercent;

    @Override
    public String parseTvShow(String text) {
        return checkExisting(PathsProvider.getTvShowsPath(), text);
    }

    @Override
    public String parseMovie(String text) {
        return checkExisting(PathsProvider.getMoviesPath(), text);
    }

    private String checkExisting(String path, String filename) {
        if (path == null) {
            return "";
        }
        return probeExistingFolder(PathsProvider.getPath(path), filename)
                .orElse(path + "/" + filename);
    }

    private Optional<String> probeExistingFolder(Path path, String filename) {
        int maxCoefficient = 0;
        Path selectedFolder = null;
        for (Path dirPath : findDirectories(path)) {
            int currentCoefficient = FuzzySearch.ratio(filename, dirPath.getFileName().toString());
            if (currentCoefficient > maxCoefficient) {
                maxCoefficient = currentCoefficient;
                selectedFolder = dirPath;
            }
        }

        if (selectedFolder != null && maxCoefficient >= similarityPercent) {
            return Optional.of(selectedFolder.toString());
        }
        return Optional.empty();
    }

    private List<Path> findDirectories(Path path) {
        List<Path> foundDirectories = new ArrayList<>();
        try {
            foundDirectories = Files.walk(path, 1, FileVisitOption.FOLLOW_LINKS)
                    .filter(Files::isDirectory)
                    .filter(dir -> !dir.equals(path))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundDirectories;
    }
}
