package net.cserny.videosmover.model;

import net.cserny.videosmover.helper.StaticPathsProvider;
import net.cserny.videosmover.service.helper.VideoOutputHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

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
        if (videoType == VideoType.TVSHOW) {
            return outputFolderWithoutDate;
        }

        String dateAppend = "";
        if (year != null) {
            dateAppend = String.format(" (%d)", year);
        }
        if (month != null && day != null) {
            dateAppend = String.format(" (%d-%d-%d)", year, month, day);
        }

        return outputFolderWithoutDate + dateAppend;
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

    public String getInputPathWithoutFileName() {
        String noFilenamePath = fullInputPath.replace(fileName, "");
        if (noFilenamePath.endsWith(StaticPathsProvider.SEPARATOR)) {
            noFilenamePath = noFilenamePath.substring(0, noFilenamePath.length() - 1);
        }
        return noFilenamePath;
    }

    public void setOutputFolderWithoutDateFromFilename() {
        outputFolderWithoutDate = fileName.contains(".")
                ? fileName.substring(0, fileName.lastIndexOf('.'))
                : fileName;
    }

    public void setDateFromReleaseDate(String releaseDate) {
        if (StringUtils.isEmpty(releaseDate)) {
            return;
        }

        Matcher matcher = VideoOutputHelper.RELEASE_DATE.matcher(releaseDate);
        if (matcher.find()) {
            String year = matcher.group("year");
            String month = matcher.group("month");
            String day = matcher.group("day");

            this.year = Integer.valueOf(year);
            if (StringUtils.isNumeric(month)) {
                this.month = Integer.valueOf(month);
            }
            if (StringUtils.isNumeric(day)) {
                this.day = Integer.valueOf(day);
            }
        }
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
