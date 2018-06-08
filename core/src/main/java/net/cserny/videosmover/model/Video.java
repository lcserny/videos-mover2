package net.cserny.videosmover.model;

import java.nio.file.Path;
import java.util.List;

public class Video {

    private Path input;
    private Path output;
    private List<Path> subtitles;
    private VideoType videoType;

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

    public VideoType getVideoType() {
        return videoType;
    }

    public void setVideoType(VideoType videoType) {
        this.videoType = videoType;
    }

    public List<Path> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<Path> subtitles) {
        this.subtitles = subtitles;
    }
}
