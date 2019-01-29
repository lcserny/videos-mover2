package net.cserny.videosmover.service;

import net.cserny.videosmover.service.validator.VideoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@Service
public class VideoChecker {

    private List<VideoValidator> videoValidatorList;

    @Autowired
    public VideoChecker(List<VideoValidator> videoValidators) {
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
