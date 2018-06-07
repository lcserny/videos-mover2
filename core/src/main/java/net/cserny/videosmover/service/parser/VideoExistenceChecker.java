package net.cserny.videosmover.service.parser;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.cserny.videosmover.PropertiesLoader;
import net.cserny.videosmover.service.StaticPathsProvider;
import net.cserny.videosmover.service.helper.SimpleVideoOutputHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Service
public class VideoExistenceChecker implements VideoNameParser {

    private int similarityPercent;

    public VideoExistenceChecker() {
        similarityPercent = PropertiesLoader.getSimilarityPercent();
    }

    @Override
    public String parseTvShow(String text) {
        return checkExisting(StaticPathsProvider.getTvShowsPath(), text);
    }

    @Override
    public String parseMovie(String text) {
        return checkExisting(StaticPathsProvider.getMoviesPath(), text);
    }

    private String checkExisting(String path, String filename) {
        return path != null
                ? probeExistingFolder(StaticPathsProvider.getPath(path), filename).orElse(path + "/" + filename)
                : "";
    }

    private Optional<String> probeExistingFolder(Path path, String filename) {
        int maxCoefficient = 0;
        Path selectedFolder = null;
        for (Path dirPath : findDirectories(path)) {
            int currentCoefficient = FuzzySearch.ratio(trimReleaseDate(filename), trimReleaseDate(dirPath.getFileName().toString()));
            if (currentCoefficient > maxCoefficient) {
                maxCoefficient = currentCoefficient;
                selectedFolder = dirPath;
            }
        }

        return selectedFolder != null && maxCoefficient >= similarityPercent
                ? Optional.of(selectedFolder.toString())
                : Optional.empty();
    }

    private String trimReleaseDate(String filename) {
        Matcher matcher = SimpleVideoOutputHelper.RELEASEDATE_PATTERN.matcher(filename);
        if (matcher.find()) {
            filename = filename.substring(0, matcher.start(1) - 2);
        }
        return filename;
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
