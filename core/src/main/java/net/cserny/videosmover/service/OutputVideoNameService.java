package net.cserny.videosmover.service;

import net.cserny.videosmover.model.Video;

import java.io.IOException;

public interface OutputVideoNameService {
    String resolve(Video video);
}
