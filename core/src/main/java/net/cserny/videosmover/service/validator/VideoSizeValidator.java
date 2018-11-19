package net.cserny.videosmover.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.cserny.videosmover.helper.PropertiesLoader;

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
