package net.cserny.videosmover.core.service;

import net.cserny.videosmover.core.model.Video;
import net.cserny.videosmover.core.service.observer.VideoAdjustmentObserver;
import net.cserny.videosmover.core.service.parser.VideoNameParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutputResolver {

    private List<VideoNameParser> nameParserList;

    @Autowired
    public OutputResolver(List<VideoNameParser> nameParserList) {
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
