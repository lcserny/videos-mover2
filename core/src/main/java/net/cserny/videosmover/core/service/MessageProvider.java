package net.cserny.videosmover.core.service;

import net.cserny.videosmover.core.constants.MessageType;
import net.cserny.videosmover.core.model.Message;

public class MessageProvider {

    public static Message inputMissing() {
        return new Message(MessageType.ERROR,
                "Downloads folder doesn't exist, please set correct path and try again.",
                "Input Path Error");
    }

    public static Message outputMissing() {
        return new Message(MessageType.ERROR,
                "Movies or TvShows folder/s not set, please set correct paths and try again.",
                "Output Path Error");
    }

    public static Message nothingSelected() {
        return new Message(MessageType.INFO,
                "No video files have been selected, nothing was moved...",
                "No Move Done");
    }

    public static Message problemOccurred() {
        return new Message(MessageType.WARNING,
                "Problem occurred while moving, some files might not have been moved, please check",
                "Move Error Detected");
    }

    public static Message moveSuccessful() {
        return new Message(MessageType.INFO,
                "Selected video files have been moved successfully",
                "Move Successful");
    }

    public static Message incorrectTvShowFileName(String videoName) {
        return new Message(MessageType.INFO,
                "TvShow " + videoName + " does not have season and / or episode info in filename",
                "Incomplete TvShow filename");
    }

    public static Message cleanupFailed() {
        return new Message(MessageType.WARNING,
                "Problem occurred while cleaning, probably video parent contains un-removable files, please check",
                "Clean Error Detected");
    }

    public static Message noMetadataServiceApiKey() {
        return new Message(MessageType.INFO,
                "No API key provided for the metadataService implementation, please specify one in properties or environment",
                "No API key found");
    }

    public static Message existingFolderFound(String existingFolder) {
        return new Message(MessageType.INFO,
                String.format("Video folder '%s' already exists, are you sure you want to move the video?", existingFolder),
                "Video folder already exists");
    }

    public static Message invalidManualPathSpecified(String manualPathSpecified) {
        return new Message(MessageType.WARNING,
                String.format("Manual path specified '%s' is invalid, falling back to previous valid path", manualPathSpecified),
                "Invalid manual path specified");
    }

    public static Message removalNotAllowed(String inputPath) {
        return new Message(MessageType.INFO,
                String.format("Video folder '%s' will not be deleted, restricted removal path detected", inputPath),
                "Path removal not allowed");
    }
}
