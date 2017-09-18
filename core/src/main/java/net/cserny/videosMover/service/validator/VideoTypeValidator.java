package net.cserny.videosMover.service.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by leonardo on 10.09.2017.
 */
@Service
@Order(2)
public class VideoTypeValidator implements VideoValidator {
    @Value("#{'${video.mime.types}'.split(',')}")
    private List<String> allowedMimeTypes;

    @Override
    public boolean isValid(Path file) throws IOException {
        String mimeType = Files.probeContentType(file);
        return mimeTypeIsAllowed(mimeType);
    }

    private boolean mimeTypeIsAllowed(String mimeType) {
        for (String allowedType : allowedMimeTypes) {
            if (allowedType.equals(mimeType)) {
                return true;
            }
        }
        return false;
    }
}
