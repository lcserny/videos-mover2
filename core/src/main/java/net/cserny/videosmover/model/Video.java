package net.cserny.videosmover.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Video {

    private String fileName;
    private String fullInputPath;
    private String outputFolderWithoutDate;
    private Integer year;
    private Integer month;
    private Integer day;
    private List<Subtitle> subtitles = new ArrayList<>();
    private VideoType videoType = VideoType.NONE;

    public Video(String fileName, String fullInputPath) {
        this.fileName = fileName;
        this.fullInputPath = fullInputPath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFullInputPath() {
        return fullInputPath;
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

    public String getOutputFolderWithoutDate() {
        return outputFolderWithoutDate;
    }

    public void setOutputFolderWithoutDate(String outputFolderWithoutDate) {
        this.outputFolderWithoutDate = outputFolderWithoutDate;
    }

    public String getOutputFolderWithDate() {
        return outputFolderWithoutDate; // TODO + append date stuff
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(fileName, video.fileName) &&
                Objects.equals(fullInputPath, video.fullInputPath) &&
                Objects.equals(outputFolderWithoutDate, video.outputFolderWithoutDate) &&
                Objects.equals(year, video.year) &&
                Objects.equals(month, video.month) &&
                Objects.equals(day, video.day) &&
                Objects.equals(subtitles, video.subtitles) &&
                videoType == video.videoType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, fullInputPath, outputFolderWithoutDate, year, month, day, subtitles, videoType);
    }

    @Override
    public String toString() {
        return "Video{" +
                "fileName='" + fileName + '\'' +
                ", fullInputPath='" + fullInputPath + '\'' +
                ", outputFolderWithoutDate='" + outputFolderWithoutDate + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", subtitles=" + subtitles +
                ", videoType=" + videoType +
                '}';
    }
}
