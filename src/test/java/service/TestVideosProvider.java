package service;

public class TestVideosProvider
{
    static {
        // TODO: this doesn't need to be static, make proper class
        // TODO: use in-memory file system (https://github.com/google/jimfs) to generate files in /mnt/Data/Downloads (in-memory) and maybe the Movies and TvShows folder also
        // TODO: put in there, each file needed from below
        // TODO: regenerate the in-memory FS in Setup before each test
        // TODO: change these return types to Path
    }


    public static String getTvShowFilePath() {
        return "/mnt/Data/Downloads/www.Torrenting.com - Criminal.Minds.S12E22.HDTV.x264-SVA/Crimil.Minds.S12E22.HDTV.x264-SVA.mkv";
    }

    public static String getMovieFilePath() {
        return "/mnt/Data/Downloads/71 [2014]/71 bla bla.mkv";
    }

    public static String getMovieSubtitleFilePath() {
        return "/mnt/Data/Downloads/71 (2014) [1080p]/71.2014.1080p.BluRay.x264.YIFY.srt";
    }

    public static String getSingleMovieDirectoryDirectoryPath() {
        return "/mnt/Data/Downloads/71 (2014) [1080p]";
    }

    public static String getSingleMovieDirectoryMovieFile() {
        return "71 bla bla.mkv";
    }

    public static String getMovieWithSubtitleFilePath() {
        return "/mnt/Data/Downloads/71 (2014) [1080p]/71.2014.1080p.BluRay.x264.YIFY.mp4";
    }

    public static String getVideoFromDownloadsRootFilePath() {
        return "/mnt/Data/Downloads/something.mp4";
    }

    public static String getSmallMovieFilePath() {
        return "/mnt/Data/some small video file < 50mb .mp4";
    }

    public static String getVideoFromDisallowedFilePath() {
        return "/mnt/Data/Programming Stuff/something.mp4";
    }

    public static String getNonVideoFilePath() {
        return "/etc/hosts";
    }

    public static String getDirectoryPath() {
        return "/mnt";
    }
}
