package net.cserny.videosmover.core.model;

import java.util.Objects;

public class Subtitle {

    private String fileName;
    private String subFolder;
    private String fullInputPath;

    public Subtitle(String fileName, String fullInputPath) {
        this.fileName = fileName;
        this.fullInputPath = fullInputPath;
    }

    public String getSubFolder() {
        return subFolder;
    }

    public void setSubFolder(String subFolder) {
        this.subFolder = subFolder;
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
                Objects.equals(subFolder, subtitle.subFolder) &&
                Objects.equals(fullInputPath, subtitle.fullInputPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, subFolder, fullInputPath);
    }

    @Override
    public String toString() {
        return "Subtitle{" +
                "fileName='" + fileName + '\'' +
                ", subFolder='" + subFolder + '\'' +
                ", fullInputPath='" + fullInputPath + '\'' +
                '}';
    }
}
