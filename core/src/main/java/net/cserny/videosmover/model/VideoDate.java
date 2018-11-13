package net.cserny.videosmover.model;

import java.util.Objects;

public class VideoDate {

    private int year;
    private int month;
    private int day;

    public VideoDate() {
    }

    public VideoDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoDate videoDate = (VideoDate) o;
        return year == videoDate.year &&
                month == videoDate.month &&
                day == videoDate.day;
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
