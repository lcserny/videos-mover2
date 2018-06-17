package net.cserny.videosmover.model;

import java.util.Objects;

public class VideoPath {

    public static final VideoPath emptyVideoPath = new VideoPath("", "", "");

    private String outputPath;
    private String outputFolder;
    private String year;

    public VideoPath() {
        this(null, null, "");
    }

    public VideoPath(String outputPath, String outputFolder, String year) {
        this.outputPath = outputPath;
        this.outputFolder = outputFolder;
        this.year = year;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isEmpty() {
        return this == emptyVideoPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoPath videoPath = (VideoPath) o;
        return Objects.equals(outputPath, videoPath.outputPath) &&
                Objects.equals(outputFolder, videoPath.outputFolder) &&
                Objects.equals(year, videoPath.year);
    }

    @Override
    public int hashCode() {

        return Objects.hash(outputPath, outputFolder, year);
    }
}
