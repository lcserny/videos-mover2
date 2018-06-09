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

    private final Video video;
    private StringProperty name = new SimpleStringProperty();
    private StringProperty output = new SimpleStringProperty();
    private BooleanProperty isMovie = new SimpleBooleanProperty();
    private BooleanProperty isTvShow = new SimpleBooleanProperty();

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
        this.isMovie.set(isMovie);
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
