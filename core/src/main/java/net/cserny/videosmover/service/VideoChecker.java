package net.cserny.videosmover.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.cserny.videosmover.service.validator.VideoValidator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

@Singleton
public class VideoChecker {

    private Set<VideoValidator> videoValidatorList;

    @Inject
    public VideoChecker(Set<VideoValidator> videoValidators) {
        this.videoValidatorList = videoValidators;
    }

    public boolean isVideo(Path file) {
        for (VideoValidator videoValidator : videoValidatorList) {
            if (!videoValidator.isValid(file)) {
                return false;
            }
        }
        return true;
    }
}
