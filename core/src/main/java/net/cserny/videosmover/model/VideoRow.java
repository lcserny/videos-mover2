package net.cserny.videosmover.model;

import javafx.beans.property.*;
import net.cserny.videosmover.facade.MainFacade;
import net.cserny.videosmover.helper.StaticPathsProvider;

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
        this.output.set(video.getOutputFolderWithDate());
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
