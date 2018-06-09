package net.cserny.videosmover.model;

public enum VideoType {
    MOVIE("Movie"), TVSHOW("TV"), NONE("None");

    private final String value;

    VideoType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
