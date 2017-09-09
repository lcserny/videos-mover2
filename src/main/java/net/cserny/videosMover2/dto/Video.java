package net.cserny.videosMover2.dto;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by leonardo on 02.09.2017.
 */
public class Video
{
    private AbstractSimpleFile input;
    private AbstractSimpleFile output;
    private List<AbstractSimpleFile> subtitles;
    private boolean isTvShow;
    private boolean isMovie;

    public AbstractSimpleFile getInput() {
        return input;
    }

    public void setInput(AbstractSimpleFile input) {
        this.input = input;
    }

    public AbstractSimpleFile getOutput() {
        return output;
    }

    public void setOutput(AbstractSimpleFile output) {
        this.output = output;
    }

    public void setIsTvShow(boolean isTvShow) {
        this.isTvShow = isTvShow;
    }

    public boolean isTvShow() {
        return isTvShow;
    }

    public void setIsMovie(boolean isMovie) {
        this.isMovie = isMovie;
    }

    public boolean isMovie() {
        return isMovie;
    }

    public List<AbstractSimpleFile> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<AbstractSimpleFile> subtitles) {
        this.subtitles = subtitles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Video video = (Video) o;

        if (isTvShow != video.isTvShow) return false;
        if (isMovie != video.isMovie) return false;
        if (input != null ? !input.equals(video.input) : video.input != null) return false;
        if (output != null ? !output.equals(video.output) : video.output != null) return false;
        return subtitles != null ? subtitles.equals(video.subtitles) : video.subtitles == null;
    }

    @Override
    public int hashCode() {
        int result = input != null ? input.hashCode() : 0;
        result = 31 * result + (output != null ? output.hashCode() : 0);
        result = 31 * result + (subtitles != null ? subtitles.hashCode() : 0);
        result = 31 * result + (isTvShow ? 1 : 0);
        result = 31 * result + (isMovie ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Video{" +
                "input=" + input +
                ", output=" + output +
                ", subtitles=" + subtitles +
                ", isTvShow=" + isTvShow +
                ", isMovie=" + isMovie +
                '}';
    }
}
