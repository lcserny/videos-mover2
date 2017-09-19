package net.cserny.videosmover.service;


import net.cserny.videosmover.service.validator.VideoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by leonardo on 02.09.2017.
 */
@Service
public class VideoCheckerImpl implements VideoChecker {
    private final List<VideoValidator> videoValidatorList;

    @Autowired
    public VideoCheckerImpl(List<VideoValidator> videoValidators) {
        this.videoValidatorList = videoValidators;
    }

    @Override
    public boolean isVideo(Path file) throws IOException {
        for (VideoValidator videoValidator : videoValidatorList) {
            if (!videoValidator.isValid(file)) {
                return false;
            }
        }
        return true;
    }
}
