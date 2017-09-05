package net.cserny.videosMover2.service;

import com.google.inject.Singleton;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.cserny.videosMover2.dto.Video;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leonardo on 02.09.2017.
 */
@Singleton
public class OutputNameResolverImpl extends ResourceInitializer implements OutputNameResolver
{
    public static final String RESOURCE_NAME_PARTS = "name_parts.cfg";
    public static final int SIMILARITY_PERCENT = 80;

    private Pattern videoPattern = Pattern.compile("(.*)(\\d{4})");
    private List<String> nameTrimParts;

    public OutputNameResolverImpl() {
        nameTrimParts = fillListFromResource(RESOURCE_NAME_PARTS);
    }

    @Override
    public String resolveTvShow(Video video) {
        String partiallyResolved = resolvePartial(video);
        return checkExisting(SystemPathsProvider.getTvShowsPath(), partiallyResolved);
    }

    @Override
    public String resolveMovie(Video video) {
        String partiallyResolved = resolvePartial(video);
        String yearAppended = appendYear(partiallyResolved);
        return checkExisting(SystemPathsProvider.getMoviesPath(), yearAppended);
    }

    private String resolvePartial(Video video) {
        String fileNameString = video.getInput().getFileName().toString();
        String trimmed = trim(fileNameString);
        String withoutExtension = removeExtension(trimmed);
        return toCamelCase(withoutExtension);
    }

    private String removeExtension(String test) {
        return test.substring(0, test.length() - 4);
    }

    private String appendYear(String videoName) {
        Matcher matcher = videoPattern.matcher(videoName);
        if (matcher.find()) {
            videoName = matcher.group(1);
            String yearString = matcher.group(2);
            if (yearString != null) {
                videoName = String.format("%s (%s)", videoName, yearString);
            }
        }
        return videoName;
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

    private String trim(String filename) {
        for (String part : nameTrimParts) {
            Pattern compile = Pattern.compile("(?i)(" + part + ")");
            Matcher matcher = compile.matcher(filename);
            if (!part.isEmpty() && matcher.find()) {
                filename = filename.substring(0, matcher.start());
            }
        }
        return filename;
    }

    private String toCamelCase(String text) {
        StringBuilder camelCaseString = new StringBuilder();
        for (String part : stripSpecialChars(text).split("\\s+")) {
            camelCaseString.append(toProperCase(part)).append(" ");
        }
        return camelCaseString.toString().trim();
    }

    private String stripSpecialChars(String videoName) {
        return videoName.replaceAll("([._])", " ").trim();
    }

    private String toProperCase(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
