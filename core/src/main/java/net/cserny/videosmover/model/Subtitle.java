package net.cserny.videosmover.model;

import java.util.Objects;

public class Subtitle {

    private String subFolder;
    private String fullInputPath;

    public Subtitle(String subFolder, String fullInputPath) {
        this.subFolder = subFolder;
        this.fullInputPath = fullInputPath;
    }

    public String getSubFolder() {
        return subFolder;
    }

    public String getFullInputPath() {
        return fullInputPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtitle subtitle = (Subtitle) o;
        return Objects.equals(subFolder, subtitle.subFolder) &&
                Objects.equals(fullInputPath, subtitle.fullInputPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subFolder, fullInputPath);
    }

    @Override
    public String toString() {
        return "Subtitle{" +
                "subFolder='" + subFolder + '\'' +
                ", fullInputPath='" + fullInputPath + '\'' +
                '}';
    }
}
