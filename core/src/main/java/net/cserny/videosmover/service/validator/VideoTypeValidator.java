package net.cserny.videosmover.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.cserny.videosmover.helper.PropertiesLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Singleton
public class VideoTypeValidator implements VideoValidator {

    private List<String> allowedMimeTypes;

    @Inject
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
