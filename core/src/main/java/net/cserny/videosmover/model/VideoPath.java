package net.cserny.videosmover.model;

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
}
