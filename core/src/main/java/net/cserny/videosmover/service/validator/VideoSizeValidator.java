package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.helper.PropertiesLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Order(2)
@Component
public class VideoSizeValidator implements VideoValidator {

    private Long minimumVideoSize;

    @Autowired
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
