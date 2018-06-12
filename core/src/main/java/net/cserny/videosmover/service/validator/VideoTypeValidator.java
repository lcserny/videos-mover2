package net.cserny.videosmover.service.validator;

import net.cserny.videosmover.helper.PropertiesLoader;

import javax.inject.Inject;
import javax.inject.Singleton;
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
