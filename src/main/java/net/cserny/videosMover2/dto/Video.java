package net.cserny.videosMover2.dto;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by leonardo on 02.09.2017.
 */
public class Video
{
    private Path input;
    private Path output;
    private List<Path> subtitles;
    private boolean isTvShow;
    private boolean isMovie;

    public Path getInput() {
        return input;
    }

    public void setInput(Path input) {
        this.input = input;
    }

    public Path getOutput() {
        return output;
    }

    public void setOutput(Path output) {
        this.output = output;
    }

    public void setIsTvShow(boolean isTvShow) {
        this.isTvShow = isTvShow;
    }

    public boolean isTvShow() {
        return isTvShow;
    }

    public void setTvShow(boolean isTvShow) {
        this.isTvShow = isTvShow;
    }

    public void setIsMovie(boolean isMovie) {
        this.isMovie = isMovie;
    }

    public boolean isMovie() {
        return isMovie;
    }

    public void setMovie(boolean isMovie) {
        this.isMovie = isMovie;
    }

    public List<Path> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<Path> subtitles) {
        this.subtitles = subtitles;
    }
}
