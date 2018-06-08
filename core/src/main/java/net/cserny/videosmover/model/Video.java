package net.cserny.videosmover.model;

import java.nio.file.Path;
import java.util.List;

public class Video {

    private Path inputPath;
    private String inputFilename;
    private Path outputPath;
    private String outputFilename;
    private List<Path> subtitles;
    private VideoType videoType;

    public Path getInputPath() {
        return inputPath;
    }

    public void setInputPath(Path inputPath) {
        this.inputPath = inputPath;
    }

    public String getInputFilename() {
        return inputFilename;
    }

    public void setInputFilename(String inputFilename) {
        this.inputFilename = inputFilename;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(Path outputPath) {
        this.outputPath = outputPath;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    public VideoType getVideoType() {
        return videoType;
    }

    public void setVideoType(VideoType videoType) {
        this.videoType = videoType;
    }

    public List<Path> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<Path> subtitles) {
        this.subtitles = subtitles;
    }
}
