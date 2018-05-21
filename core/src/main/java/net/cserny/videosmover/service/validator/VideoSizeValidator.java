package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.PropertiesLoader;

import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
public class VideoSizeValidator implements VideoValidator {

    private Long minimumVideoSize;

    public VideoSizeValidator() {
        minimumVideoSize = PropertiesLoader.getMinimumVideoSize();
    }

    @Override
    public boolean isValid(Path file) throws IOException {
        long size = Files.size(file);
        return size > minimumVideoSize;
    }
}
