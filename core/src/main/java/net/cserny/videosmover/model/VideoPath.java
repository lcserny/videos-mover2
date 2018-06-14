package net.cserny.videosmover.model;

public class VideoPath {

    private String outputPath;
    private String outputFolder;

    public VideoPath() {
        this(null, null);
    }

    public VideoPath(String outputPath, String outputFolder) {
        this.outputPath = outputPath;
        this.outputFolder = outputFolder;
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
}
