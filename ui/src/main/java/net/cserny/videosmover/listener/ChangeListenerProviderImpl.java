package net.cserny.videosmover.listener;

import net.cserny.videosmover.model.VideoRow;
import net.cserny.videosmover.service.OutputNameResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangeListenerProviderImpl implements ChangeListenerProvider {
    private final OutputNameResolver nameResolver;

    @Autowired
    public ChangeListenerProviderImpl(OutputNameResolver nameResolver) {
        this.nameResolver = nameResolver;
    }

    @Override
    public MovieChangeListener getMovieChangeListener(VideoRow videoRow) {
        return new MovieChangeListener(videoRow, nameResolver);
    }

    @Override
    public TvShowChangeListener getTvShowChangeListener(VideoRow videoRow) {
        return new TvShowChangeListener(videoRow, nameResolver);
    }
}
