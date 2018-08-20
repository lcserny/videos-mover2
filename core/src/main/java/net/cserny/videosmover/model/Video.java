package net.cserny.videosmover.model;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Video {

    private Path inputPath;
    private String inputFilename;
    private Path outputPath;
    private String outputFolderName;
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

    public String getOutputFolderName() {
        return outputFolderName;
    }

    public void setOutputFolderName(String outputFolderName) {
        this.outputFolderName = outputFolderName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(inputPath, video.inputPath) &&
                Objects.equals(inputFilename, video.inputFilename) &&
                Objects.equals(outputPath, video.outputPath) &&
                Objects.equals(outputFolderName, video.outputFolderName) &&
                Objects.equals(subtitles, video.subtitles) &&
                videoType == video.videoType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputPath, inputFilename, outputPath, outputFolderName, subtitles, videoType);
    }

    @Override
    public String toString() {
        return "Video{" +
                "inputPath=" + inputPath +
                ", inputFilename='" + inputFilename + '\'' +
                ", outputPath=" + outputPath +
                ", outputFolderName='" + outputFolderName + '\'' +
                ", subtitles=" + subtitles +
                ", videoType=" + videoType +
                '}';
    }
}
