package net.cserny.videosmover.model;

import javafx.beans.property.*;

public class VideoRow {

    private final Video video;
    private StringProperty name = new SimpleStringProperty();
    private StringProperty output = new SimpleStringProperty();
    private SimpleObjectProperty<VideoType> videoType = new SimpleObjectProperty<>();

    public VideoRow(Video video) {
        this.video = video;
    }

    public Video getVideo() {
        return video;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getOutput() {
        return output.get();
    }

    public StringProperty outputProperty() {
        return output;
    }

    public void setOutput(String output) {
        this.output.set(output);
    }

    public VideoType getVideoType() {
        return videoType.get();
    }

    public SimpleObjectProperty<VideoType> videoTypeProperty() {
        return videoType;
    }

    public void setVideoType(VideoType videoType) {
        this.videoType.set(videoType);
    }
}
