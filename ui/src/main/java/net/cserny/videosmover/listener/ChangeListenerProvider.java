package net.cserny.videosmover.listener;

import net.cserny.videosmover.model.VideoRow;

public interface ChangeListenerProvider {
    MovieChangeListener getMovieChangeListener(VideoRow videoRow);

    TvShowChangeListener getTvShowChangeListener(VideoRow videoRow);
}
