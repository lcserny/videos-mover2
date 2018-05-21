package net.cserny.videosmover.service;

import net.cserny.videosmover.service.validator.VideoValidator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Singleton
public class VideoChecker {

    private final List<VideoValidator> videoValidatorList;

    @Inject
    public VideoChecker(List<VideoValidator> videoValidators) {
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
