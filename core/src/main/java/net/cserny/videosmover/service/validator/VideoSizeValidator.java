package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.PropertiesLoader;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
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
