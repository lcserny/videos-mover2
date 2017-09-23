package net.cserny.videosmover.model;

public class SimpleVideoOutput {
    private String name;
    private Integer year;
    private String path;
    private VideoType videoType;

    public SimpleVideoOutput(String name, Integer year, String path, VideoType videoType) {
        this.name = name;
        this.year = year;
        this.path = path;
        this.videoType = videoType;
    }

    public String getName() {
        return name;
    }

    public Integer getYear() {
        return year;
    }

    public String getPath() {
        return path;
    }

    public VideoType getVideoType() {
        return videoType;
    }
}
