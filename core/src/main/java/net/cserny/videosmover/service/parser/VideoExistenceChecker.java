package net.cserny.videosmover.service.parser;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.cserny.videosmover.helper.PropertiesLoader;
import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.model.VideoDate;
import net.cserny.videosmover.service.MessageProvider;
import net.cserny.videosmover.service.SimpleMessageRegistry;
import net.cserny.videosmover.service.helper.SimpleVideoOutputHelper;
import net.cserny.videosmover.service.observer.VideoAdjustmentObserver;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        String existing = checkExisting(StaticPathsProvider.getTvShowsPath(), video, observers);
        video.setOutputFolderName(existing);
    }

    @Override
    public void parseMovie(Video video, List<VideoAdjustmentObserver> observers) {
        String existing = checkExisting(StaticPathsProvider.getMoviesPath(), video, observers);
        video.setOutputFolderName(existing);
    }

    private String checkExisting(String path, Video video, List<VideoAdjustmentObserver> observers) {
        Optional<String> existingFolder = probeExistingFolder(StaticPathsProvider.getPath(path), video.getOutputFolderName());
        if (existingFolder.isPresent()) {
            for (VideoAdjustmentObserver observer : observers) {
                observer.dontAdjustPath();
            }
            if (!path.equals(StaticPathsProvider.getTvShowsPath())) {
                messageRegistry.displayMessage(MessageProvider.existingFolderFound(existingFolder.get()));
            }

            populateYear(existingFolder.get(), video);
            return existingFolder.get();
        }
        return video.getOutputFolderName();
    }

    private void populateYear(String existingFolder, Video video) {
        VideoDate date = video.getDate();
        Matcher releaseDateMatcher = SimpleVideoOutputHelper.RELEASE_DATE.matcher(existingFolder);
        Matcher yearMatcher = SimpleVideoOutputHelper.YEAR_ONLY.matcher(existingFolder);
        if (releaseDateMatcher.find()) {
            date.setYear(Integer.valueOf(releaseDateMatcher.group("year")));
            date.setMonth(Integer.valueOf(releaseDateMatcher.group("month")));
            date.setDay(Integer.valueOf(releaseDateMatcher.group("day")));
        } else if (yearMatcher.find()) {
            date.setYear(Integer.valueOf(releaseDateMatcher.group("year")));
        }
    }

    private Optional<String> probeExistingFolder(Path path, String filename) {
        int maxCoefficient = 0;
        Path selectedFolder = null;
        for (Path dirPath : findDirectories(path)) {
            int currentCoefficient = FuzzySearch.ratio(trimReleaseDate(filename), trimReleaseDate(dirPath.getFileName().toString()));
            if (currentCoefficient > maxCoefficient) {
                maxCoefficient = currentCoefficient;
                selectedFolder = dirPath.getFileName();
            }
        }

        return selectedFolder != null && maxCoefficient >= similarityPercent
                ? Optional.of(selectedFolder.toString())
                : Optional.empty();
    }

    private String trimReleaseDate(String filename) {
        Matcher matcher = SimpleVideoOutputHelper.NAME_WITH_RELEASE_DATE.matcher(filename);
        if (matcher.find()) {
            filename = matcher.group("name");
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
