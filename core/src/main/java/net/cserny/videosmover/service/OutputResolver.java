package net.cserny.videosmover.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.cserny.videosmover.model.Video;
import net.cserny.videosmover.service.observer.VideoAdjustmentObserver;
import net.cserny.videosmover.service.parser.VideoNameParser;

import java.util.List;
import java.util.Set;

@Singleton
public class OutputResolver {

    private Set<VideoNameParser> nameParserList;

    @Inject
    public OutputResolver(Set<VideoNameParser> nameParserList) {
        this.nameParserList = nameParserList;
    }

    public void resolve(Video video, List<VideoAdjustmentObserver> observers) {
        if (video.getOutputFolderWithoutDate() == null) {
            video.setOutputFolderWithoutDateFromFilename();
        }
        adjustOutputFolderName(video, observers);
    }

    private void adjustOutputFolderName(Video video, List<VideoAdjustmentObserver> observers) {
        // TODO: improve this, 3 parser for movie and 3 for Tv, more OOP
        // maybe add pe video un Config object care stie sa puna path si etc specific cna d se bifeaza videoType?
        for (VideoNameParser videoNameParser : nameParserList) {
            switch (video.getVideoType()) {
                case MOVIE:
                    videoNameParser.parseMovie(video, observers);
                    break;
                case TVSHOW:
                    videoNameParser.parseTvShow(video, observers);
                    break;
            }
        }
    }
}
