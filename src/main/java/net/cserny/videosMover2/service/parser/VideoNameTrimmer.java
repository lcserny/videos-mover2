package net.cserny.videosMover2.service.parser;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.cserny.videosMover2.service.SystemPathsProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by leonardo on 10.09.2017.
 */
@Component
public class VideoNameTrimmer implements VideoNameParser
{
    public static final int SIMILARITY_PERCENT = 80;

    @Override
    public String parseTvShow(String text) {
        return checkExisting(SystemPathsProvider.getTvShowsPath(), text);
    }

    @Override
    public String parseMovie(String text) {
        return checkExisting(SystemPathsProvider.getMoviesPath(), text);
    }

    private String checkExisting(String path, String filename) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(path), Files::isDirectory)) {
            int maxCoefficient = 0;
            Path selectedFolder = null;

            for (Path dirPath : directoryStream) {
                int currentCoefficient = FuzzySearch.ratio(filename, dirPath.getFileName().toString());
                if (currentCoefficient > maxCoefficient) {
                    maxCoefficient = currentCoefficient;
                    selectedFolder = dirPath;
                }
            }

            if (selectedFolder != null && maxCoefficient >= SIMILARITY_PERCENT) {
                return selectedFolder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path + "/" + filename;
    }
}
