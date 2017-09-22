package net.cserny.videosmover.listener;

import net.cserny.videosmover.component.CustomTextFieldCell;
import net.cserny.videosmover.model.VideoRow;
import net.cserny.videosmover.service.OutputResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoTypeChangeListener implements ChangeListenerProvider {
    private final OutputResolver outputResolver;

    @Autowired
    public VideoTypeChangeListener(OutputResolver outputResolver) {
        this.outputResolver = outputResolver;
    }

    @Override
    public MovieChangeListener getMovieChangeListener(VideoRow videoRow, CustomTextFieldCell outputCell) {
        return new MovieChangeListener(videoRow, outputCell, outputResolver);
    }

    @Override
    public TvShowChangeListener getTvShowChangeListener(VideoRow videoRow, CustomTextFieldCell outputCell) {
        return new TvShowChangeListener(videoRow, outputCell, outputResolver);
    }
}
