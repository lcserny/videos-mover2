package net.cserny.videosmover.service;

import javafx.scene.control.Alert;
import net.cserny.videosmover.model.Message;

public class MessageProvider {

    public static Message inputMissing() {
        return new Message(Alert.AlertType.ERROR,
                "Downloads folder doesn't exist, please set correct path and try again.",
                "Input Path Error");
    }

    public static Message outputMissing() {
        return new Message(Alert.AlertType.ERROR,
                "Movies or TvShows folder/s not set, please set correct paths and try again.",
                "Output Path Error");
    }

    public static Message nothingSelected() {
        return new Message(Alert.AlertType.INFORMATION,
                "No video files have been selected, nothing was moved...",
                "No Move Done");
    }

    public static Message problemOccurred() {
        return new Message(Alert.AlertType.WARNING,
                "Problem occurred while moving, some files might not have been moved, please check",
                "Move Error Detected");
    }

    public static Message moveSuccessful() {
        return new Message(Alert.AlertType.INFORMATION,
                "Selected video files have been moved successfully",
                "Move Successful");
    }

    public static Message incorrectTvShowFileName(String videoName) {
        return new Message(Alert.AlertType.INFORMATION,
                "TvShow " + videoName + " does not have season and / or episode info in filename",
                "Incomplete TvShow filename");
    }

    public static Message cleanupFailed() {
        return new Message(Alert.AlertType.WARNING,
                "Problem occurred while cleaning, probably video parent contains un-removable files, please check",
                "Clean Error Detected");
    }

    public static Message noMetadataServiceApiKey() {
        return new Message(Alert.AlertType.INFORMATION,
                "No API key provided for the metadataService implementation, please specify one in properties or environment",
                "No API key found");
    }

    public static Message existingFolderFound(String existingFolder) {
        return new Message(Alert.AlertType.INFORMATION,
                String.format("Video folder '%s' already exists, are you sure you want to move the video?", existingFolder),
                "Video folder already exists");
    }
}
