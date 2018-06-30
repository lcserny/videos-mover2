package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.helper.PropertiesLoader;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
public class VideoSizeValidator implements VideoValidator {

    private Long minimumVideoSize;

    @Inject
    public VideoSizeValidator() {
        minimumVideoSize = PropertiesLoader.getMinimumVideoSize();
    }

    @Override
    public boolean isValid(Path file) {
        try {
            long size = Files.size(file);
            return size > minimumVideoSize;
        } catch (IOException e) {
            return false;
        }
    }
}
