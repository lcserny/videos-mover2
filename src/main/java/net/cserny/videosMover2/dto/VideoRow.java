package net.cserny.videosMover2.dto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.cserny.videosMover2.service.PathsProvider;

import java.nio.file.Paths;

/**
 * Created by leonardo on 02.09.2017.
 */
public class VideoRow
{
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
        this.video.setOutput(PathsProvider.getPath(output));
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
        this.video.setIsMovie(isMovie);
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
        this.video.setIsTvShow(isTvShow);
    }
}
