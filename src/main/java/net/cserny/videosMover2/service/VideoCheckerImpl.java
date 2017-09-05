package net.cserny.videosMover2.service;

import com.google.inject.Singleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by leonardo on 02.09.2017.
 */
@Singleton
public class VideoCheckerImpl extends ResourceInitializer implements VideoChecker
{
    public static final String RESOURCE_MIMETYPES = "mime_types.cfg";
    public static final String RESOURCE_EXCLUDEPATHS = "excluded_paths.cfg";
    public static final long MIN_ALLOWED_VIDEO_SIZE = 50 * 1024 * 1024; //50mb

    private List<String> allowedMimeTypes;
    private List<String> excludedVideoPaths;

    public VideoCheckerImpl() {
        initExcludedVideoPaths();
        initAllowedMimeTypes();
    }

    private void initExcludedVideoPaths() {
        excludedVideoPaths = fillListFromResource(RESOURCE_EXCLUDEPATHS);
    }

    private void initAllowedMimeTypes() {
        allowedMimeTypes = fillListFromResource(RESOURCE_MIMETYPES);
    }

    @Override
    public boolean isVideo(Path file) throws IOException {
        boolean hasAllowedPath = hasAllowedPath(file);
        boolean hasVideoMimeType = hasVideoMimeType(file);
        boolean hasCorrectSize = hasCorrectSize(file);

        return hasAllowedPath && hasVideoMimeType && hasCorrectSize;
    }

    private boolean hasAllowedPath(Path file) {
        String pathString = file.getParent().toString();
        return pathIsAllowed(pathString);
    }

    private boolean pathIsAllowed(String pathString) {
        for (String excludePath : excludedVideoPaths) {
            if (pathString.contains(excludePath)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasCorrectSize(Path file) throws IOException {
        long size = Files.size(file);
        return size > MIN_ALLOWED_VIDEO_SIZE;
    }

    private boolean hasVideoMimeType(Path file) throws IOException {
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
