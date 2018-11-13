package net.cserny.videosmover.model;

import java.util.List;
import java.util.Objects;

public class Video {

    private VideoDate date;
    private String fileName;
    private String inputPath;
    private String inputFolderName;
    private String outputPath;
    private String outputFolderName;
    private List<Subtitle> subtitles;
    private VideoType videoType;

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

    public VideoDate getDate() {
        return date;
    }

    public void setDate(VideoDate date) {
        this.date = date;
    }

    public List<Subtitle> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<Subtitle> subtitles) {
        this.subtitles = subtitles;
    }

    public VideoType getVideoType() {
        return videoType;
    }

    public void setVideoType(VideoType videoType) {
        this.videoType = videoType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(fileName, video.fileName) &&
                Objects.equals(inputPath, video.inputPath) &&
                Objects.equals(inputFolderName, video.inputFolderName) &&
                Objects.equals(outputPath, video.outputPath) &&
                Objects.equals(outputFolderName, video.outputFolderName) &&
                Objects.equals(date, video.date) &&
                Objects.equals(subtitles, video.subtitles) &&
                videoType == video.videoType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, inputPath, inputFolderName, outputPath, outputFolderName, date, subtitles, videoType);
    }

    @Override
    public String toString() {
        return "Video{" +
                "fileName='" + fileName + '\'' +
                ", inputPath='" + inputPath + '\'' +
                ", inputFolderName='" + inputFolderName + '\'' +
                ", outputPath='" + outputPath + '\'' +
                ", outputFolderName='" + outputFolderName + '\'' +
                ", date=" + date +
                ", subtitles=" + subtitles +
                ", videoType=" + videoType +
                '}';
    }
}
