package net.cserny.videosmover.service;

import net.cserny.videosmover.service.validator.VideoValidator;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public class VideoChecker {

    private final Set<VideoValidator> videoValidatorList;

    @Inject
    public VideoChecker(Set<VideoValidator> videoValidators) {
        this.videoValidatorList = videoValidators;
    }

    public boolean isVideo(Path file) throws IOException {
        for (VideoValidator videoValidator : videoValidatorList) {
            if (!videoValidator.isValid(file)) {
                return false;
            }
        }
        return true;
    }
}
