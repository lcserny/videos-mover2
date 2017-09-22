package net.cserny.videosmover.listener;

import net.cserny.videosmover.component.CustomTextFieldCell;
import net.cserny.videosmover.model.VideoRow;

public interface ChangeListenerProvider {
    MovieChangeListener getMovieChangeListener(VideoRow videoRow, CustomTextFieldCell outputCell);

    TvShowChangeListener getTvShowChangeListener(VideoRow videoRow, CustomTextFieldCell outputCell);
}
