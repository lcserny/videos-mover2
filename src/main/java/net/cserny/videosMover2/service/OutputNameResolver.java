package net.cserny.videosMover2.service;

import net.cserny.videosMover2.dto.Video;

/**
 * Created by leonardo on 02.09.2017.
 */
public interface OutputNameResolver
{
    String resolveTvShow(Video video);

    String resolveMovie(Video video);
}
