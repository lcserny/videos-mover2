package net.cserny.videosmover.model;

import java.util.Objects;

public class VideoDate {

    private Integer year;
    private Integer month;
    private Integer day;

    public VideoDate() {
    }

    public VideoDate(Integer year, Integer month, Integer day) {
        this.year = year;
        this.month = month;
        this.day = day;
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
        VideoDate videoDate = (VideoDate) o;
        return Objects.equals(year, videoDate.year) &&
                Objects.equals(month, videoDate.month) &&
                Objects.equals(day, videoDate.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    @Override
    public String toString() {
        return "VideoDate{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}
