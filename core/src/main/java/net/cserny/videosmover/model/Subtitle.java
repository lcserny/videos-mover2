package net.cserny.videosmover.model;

import java.util.Objects;

/*
 * Example for subtitle D:/Downloads/SomeMovie/SomeVideo.srt:
 * ---------------------------------------------------------------
 * fileName = SomeVideo.srt
 * inputPath = D:/Downloads/SomeMovie/SomeVideo.srt
 * inputFolderName = null
 *
 * Example for subtitle D:/Downloads/SomeMovie/Subs/SomeVideo.srt:
 * ---------------------------------------------------------------
 * fileName = SomeVideo.srt
 * inputPath = D:/Downloads/SomeMovie/Subs/SomeVideo.srt
 * inputFolderName = Subs
 *
 * Example for subtitle D:/Downloads/SomeVideo.srt:
 * ---------------------------------------------------------------
 * fileName = SomeVideo.srt
 * inputPath = D:/Downloads/SomeVideo.srt
 * inputFolderName = null
 * */
public class Subtitle {

    private String fileName;
    private String inputPath;
    private String inputFolderName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getInputFolderName() {
        return inputFolderName;
    }

    public void setInputFolderName(String inputFolderName) {
        this.inputFolderName = inputFolderName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtitle subtitle = (Subtitle) o;
        return Objects.equals(fileName, subtitle.fileName) &&
                Objects.equals(inputPath, subtitle.inputPath) &&
                Objects.equals(inputFolderName, subtitle.inputFolderName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fileName, inputPath, inputFolderName);
    }

    @Override
    public String toString() {
        return "Subtitle{" +
                "fileName='" + fileName + '\'' +
                ", inputPath='" + inputPath + '\'' +
                ", inputFolderName='" + inputFolderName + '\'' +
                '}';
    }
}
