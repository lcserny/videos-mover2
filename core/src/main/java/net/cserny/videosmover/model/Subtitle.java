package net.cserny.videosmover.model;

import java.util.Objects;

public class Subtitle {

    private String fileName;
    private String fullInputPath;

    public Subtitle(String fileName, String fullInputPath) {
        this.fileName = fileName;
        this.fullInputPath = fullInputPath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFullInputPath() {
        return fullInputPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtitle subtitle = (Subtitle) o;
        return Objects.equals(fileName, subtitle.fileName) &&
                Objects.equals(fullInputPath, subtitle.fullInputPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, fullInputPath);
    }

    @Override
    public String toString() {
        return "Subtitle{" +
                "fileName='" + fileName + '\'' +
                ", fullInputPath='" + fullInputPath + '\'' +
                '}';
    }
}
