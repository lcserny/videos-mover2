package net.cserny.videosmover.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.cserny.videosmover.service.StaticPathsProvider;

import java.nio.file.Path;

/**
 * Created by leonardo on 02.09.2017.
 */
public class VideoRow {
    private int index;
    private final Video video;
    private StringProperty name = new SimpleStringProperty();
    private StringProperty output = new SimpleStringProperty();
    private BooleanProperty isMovie = new SimpleBooleanProperty();
    private BooleanProperty isTvShow = new SimpleBooleanProperty();

    public VideoRow(int index, Video video) {
        this.index = index;
        this.video = video;
    }

    public int getIndex() {
        return index;
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
        Path path = StaticPathsProvider.getPath(output);
        this.video.setOutputPath(path.getParent());
        this.video.setOutputFilename(path.getFileName().toString());
    }

    public boolean isMovie() {
        return isMovie.get();
    }

    public BooleanProperty isMovieProperty() {
        return isMovie;
    }

    public void setIsMovie(boolean isMovie) {
        if (isMovie) {
            setIsTvShow(false);
        }

        this.isMovie.set(isMovie);
        this.video.setVideoType(isMovie ? VideoType.MOVIE : VideoType.TVSHOW);
    }

    public boolean isTvShow() {
        return isTvShow.get();
    }

    public BooleanProperty isTvShowProperty() {
        return isTvShow;
    }

    public void setIsTvShow(boolean isTvShow) {
        if (isTvShow) {
            setIsMovie(false);
        }

        this.isTvShow.set(isTvShow);
        this.video.setVideoType(isTvShow ? VideoType.TVSHOW : VideoType.MOVIE);
    }
}
