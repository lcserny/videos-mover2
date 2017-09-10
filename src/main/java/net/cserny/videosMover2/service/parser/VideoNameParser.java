package net.cserny.videosMover2.service.parser;

/**
 * Created by leonardo on 10.09.2017.
 */
public interface VideoNameParser
{
    public String parseTvShow(String text);

    public String parseMovie(String text);
}
