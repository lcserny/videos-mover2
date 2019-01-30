package net.cserny.videosmover.javafx.model;

import javafx.beans.property.*;
import net.cserny.videosmover.core.model.Video;
import net.cserny.videosmover.core.model.VideoType;

public class VideoRow {

    private final Video video;
    private StringProperty name = new SimpleStringProperty();
    private StringProperty output = new SimpleStringProperty();
    private SimpleObjectProperty<VideoType> videoType = new SimpleObjectProperty<>();

    public VideoRow(Video video) {
        this.video = video;
        this.name.set(video.getFileName());
        this.videoType.set(video.getVideoType());
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

    public void setOutput(Video video) {
        this.output.set(video != null ? video.getOutputFolderWithDate() : "");
    }

    public void setOutputManual(String output) {
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
        this.video.setVideoType(videoType);
    }
}
