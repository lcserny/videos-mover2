package net.cserny.videosMover2.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leonardo on 02.09.2017.
 */
public class SubtitlesFinderImpl implements SubtitlesFinder
{
    public static final String RESOURCE_SUBTITLE_EXTENSIONS = "subtitle_extensions.cfg";

    private List<String> subtitleExtensions;

    public SubtitlesFinderImpl() {
        subtitleExtensions = initSubtitleExtensions();
    }

    private List<String> initSubtitleExtensions() {
        List<String> list = new ArrayList<>();
        try {
            list = Files.readAllLines(
                    Paths.get(getClass().getClassLoader().getResource(RESOURCE_SUBTITLE_EXTENSIONS).toURI()),
                    Charset.forName("UTF-8"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Path> find(Path file) throws IOException {
        List<Path> subtitles = new ArrayList<>();

        Path directory = file.getParent();
        if (directory.toString().equals(SystemPathsProvider.getDownloadsPath())) {
            return subtitles;
        }

        addSubtitles(subtitles, directory);
        return subtitles;
    }

    private void addSubtitles(List<Path> subtitles, Path directory) throws IOException {
        List<Path> files = Files.walk(directory).filter(Files::isRegularFile).collect(Collectors.toList());
        for (Path tmpFile : files) {
            for (String subtitleExtension : subtitleExtensions) {
                if (tmpFile.toString().endsWith(subtitleExtension)) {
                    subtitles.add(tmpFile);
                }
            }
        }
    }
}
