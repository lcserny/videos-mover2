package net.cserny.videosmover.core.service.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static net.cserny.videosmover.core.constants.PropertyConstants.VIDEO_MIME_TYPES_KEY;

@Order(1)
@Component
public class VideoTypeValidator implements VideoValidator {

    @Value("#{'${" + VIDEO_MIME_TYPES_KEY + "}'.split(',')}")
    private List<String> allowedMimeTypes;

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
