package net.cserny.videosmover.service;

import javafx.scene.control.Alert;
import net.cserny.videosmover.model.Message;

public class MessageProvider {
    public static Message getIputMissing() {
        return new Message(Alert.AlertType.ERROR,
                "Downloads folder doesn't exist, please set correct path and try again.",
                "Input Path Error");
    }

    public static Message getOutputMissing() {
        return new Message(Alert.AlertType.ERROR,
                "Movies or TvShows folder/s not set, please set correct paths and try again.",
                "Output Path Error");
    }

    public static Message getNothingSelected() {
        return new Message(Alert.AlertType.INFORMATION,
                "No video files have been selected, nothing was moved...",
                "No Move Done");
    }

    public static Message getProblemOccurred() {
        return new Message(Alert.AlertType.WARNING,
                "Problem occurred while moving, some files might not have been moved, please check",
                "Move Error Detected");
    }

    public static Message getMoveSuccessful() {
        return new Message(Alert.AlertType.INFORMATION,
                "Selected video files have been moved successfully",
                "Move Successful");
    }

    public static Message getCleanupFailed() {
        return new Message(Alert.AlertType.WARNING,
                "Problem occurred while cleaning, probably video parent contains un-removable files, please check",
                "Clean Error Detected");
    }
}
