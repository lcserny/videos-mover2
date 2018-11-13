package net.cserny.videosmover.model;

import java.util.Objects;

public class Subtitle {

    private String fileName;
    private String inputPath;
    private String inputFolderName;
    private String outputPath;
    private String outputFolderName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getInputFolderName() {
        return inputFolderName;
    }

    public void setInputFolderName(String inputFolderName) {
        this.inputFolderName = inputFolderName;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getOutputFolderName() {
        return outputFolderName;
    }

    public void setOutputFolderName(String outputFolderName) {
        this.outputFolderName = outputFolderName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtitle subtitle = (Subtitle) o;
        return Objects.equals(fileName, subtitle.fileName) &&
                Objects.equals(inputPath, subtitle.inputPath) &&
                Objects.equals(inputFolderName, subtitle.inputFolderName) &&
                Objects.equals(outputPath, subtitle.outputPath) &&
                Objects.equals(outputFolderName, subtitle.outputFolderName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fileName, inputPath, inputFolderName, outputPath, outputFolderName);
    }

    @Override
    public String toString() {
        return "Subtitle{" +
                "fileName='" + fileName + '\'' +
                ", inputPath='" + inputPath + '\'' +
                ", inputFolderName='" + inputFolderName + '\'' +
                ", outputPath='" + outputPath + '\'' +
                ", outputFolderName='" + outputFolderName + '\'' +
                '}';
    }
}
