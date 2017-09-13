package net.cserny.videosMover2.service.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by leonardo on 10.09.2017.
 */
@Service
@Order(3)
public class VideoSizeValidator implements VideoValidator
{
    @Value("${minimum.video.size}")
    private Long minimumVideoSize;

    @Override
    public boolean isValid(Path file) throws IOException {
        long size = Files.size(file);
        return size > minimumVideoSize;
    }
}
