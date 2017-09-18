package net.cserny.videosMover.service.parser;

/**
 * Created by leonardo on 10.09.2017.
 */
public interface VideoNameParser {
    String parseTvShow(String text);

    String parseMovie(String text);
}
