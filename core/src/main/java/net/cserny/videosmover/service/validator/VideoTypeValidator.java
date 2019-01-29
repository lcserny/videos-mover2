package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.helper.PropertiesLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Order(1)
@Component
public class VideoTypeValidator implements VideoValidator {

    private List<String> allowedMimeTypes;

    @Autowired
    public VideoTypeValidator() {
        allowedMimeTypes = PropertiesLoader.getVideoMimeTypes();
    }

    @Override
    public boolean isValid(Path file) {
        try {
            String mimeType = Files.probeContentType(file);
            return mimeTypeIsAllowed(mimeType);
        } catch (IOException e) {
            return false;
        }
    }

    private boolean mimeTypeIsAllowed(String mimeType) {
        for (String allowedType : allowedMimeTypes) {
            if (allowedType.equals(mimeType)) {
                return true;
            }
        }
        return mimeType != null && mimeType.startsWith("video/");
    }
}
