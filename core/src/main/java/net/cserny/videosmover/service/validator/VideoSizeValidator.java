package net.cserny.videosmover.service.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.cserny.videosmover.constants.PropertyConstants.MIN_VIDEO_SIZE_KEY;

@Order(2)
@Component
public class VideoSizeValidator implements VideoValidator {

    @Value("${" + MIN_VIDEO_SIZE_KEY + "}")
    private Long minimumVideoSize;

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
