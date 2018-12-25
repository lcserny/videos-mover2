package net.cserny.videosmover.service.parser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.cserny.videosmover.helper.PropertiesLoader;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.MessageProvider;
import net.cserny.videosmover.service.SimpleMessageRegistry;
import net.cserny.videosmover.service.observer.VideoAdjustmentObserver;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.cserny.videosmover.service.helper.VideoOutputHelper.trimReleaseDate;

@Singleton
public class VideoExistenceChecker implements VideoNameParser {

    private final SimpleMessageRegistry messageRegistry;
    private int similarityPercent;

    @Inject
    public VideoExistenceChecker(SimpleMessageRegistry messageRegistry) {
        this.messageRegistry = messageRegistry;
        similarityPercent = PropertiesLoader.getSimilarityPercent();
    }

    @Override
    public void parseTvShow(Video video, List<VideoAdjustmentObserver> observers) {
        parse(StaticPathsProvider.getTvShowsPath(), video, observers);
    }

    @Override
    public void parseMovie(Video video, List<VideoAdjustmentObserver> observers) {
        parse(StaticPathsProvider.getMoviesPath(), video, observers);
    }

    private void parse(String path, Video video, List<VideoAdjustmentObserver> observers) {
        probeExistingFolder(StaticPathsProvider.getPath(path), video.getOutputFolderWithoutDate()).ifPresent(existingFolder -> {
            for (VideoAdjustmentObserver observer : observers) {
                observer.dontAdjustPath();
            }

            if (!path.equals(StaticPathsProvider.getTvShowsPath())) {
                messageRegistry.displayMessage(MessageProvider.existingFolderFound(existingFolder));
            }

            video.setOutputFolderWithoutDate(trimReleaseDate(existingFolder));
            video.setDateFromReleaseDate(existingFolder);
        });
    }

    private Optional<String> probeExistingFolder(Path path, String outputFolderNameWithoutDate) {
        int maxCoefficient = 0;
        Path selectedFolder = null;
        for (Path dirPath : findDirectories(path)) {
            int currentCoefficient = FuzzySearch.ratio(
                    trimReleaseDate(outputFolderNameWithoutDate),
                    trimReleaseDate(dirPath.getFileName().toString()));

            if (currentCoefficient > maxCoefficient) {
                maxCoefficient = currentCoefficient;
                selectedFolder = dirPath.getFileName();
            }
        }

        return selectedFolder != null && maxCoefficient >= similarityPercent
                ? Optional.of(selectedFolder.toString())
                : Optional.empty();
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
