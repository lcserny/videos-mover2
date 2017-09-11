package net.cserny.videosMover2.service.validator;

import net.cserny.videosMover2.service.AbstractResourceInitializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by leonardo on 10.09.2017.
 */
public class VideoTypeValidator extends AbstractResourceInitializer implements VideoValidator
{
    public static final String RESOURCE_MIMETYPES = "mime_types.cfg";

    private List<String> allowedMimeTypes;

    public VideoTypeValidator() {
        allowedMimeTypes = fillListFromResource(RESOURCE_MIMETYPES);
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
