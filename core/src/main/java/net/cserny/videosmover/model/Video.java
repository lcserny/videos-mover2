package net.cserny.videosmover.model;

import java.util.List;
import java.util.Objects;

/*
* Example for movie D:/Downloads/SomeMovie/SomeVideo.mp4:
* ---------------------------------------------------------------
* fileName = SomeVideo.mp4
* inputPath = D:/Downloads/SomeMovie/SomeVideo.mp4
* inputFolderName = SomeMovie
* outputPath = D:/Movies/FormattedSomeMovie/SomeVideo.mp4
* outputFolderName = FormattedSomeMovie
*
* Example for movie D:/Downloads/SomeVideo.mp4:
* ---------------------------------------------------------------
* fileName = SomeVideo.mp4
* inputPath = D:/Downloads/SomeVideo.mp4
* inputFolderName = null
* outputPath = D:/Movies/FormattedFromNameSomeMovie/SomeVideo.mp4
* outputFolderName = FormattedFromNameSomeMovie
* */
public class Video {

    private VideoDate date = new VideoDate();
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

    public String getOutputPathWithoutFolder() {
        String noFileNameOutputPath = outputPath.replace(fileName, "");
        return noFileNameOutputPath.substring(0, noFileNameOutputPath.length() - 1);
    }

    public String getInputFolderNameFromFileName() {
        if (fileName.contains(".")) {
            return fileName.substring(0, fileName.lastIndexOf('.'));
        }
        return fileName;
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
