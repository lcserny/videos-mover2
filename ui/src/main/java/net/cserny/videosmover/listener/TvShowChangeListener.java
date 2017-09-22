package net.cserny.videosmover.listener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import net.cserny.videosmover.component.CustomTextFieldCell;
import net.cserny.videosmover.model.VideoRow;
import net.cserny.videosmover.service.OutputResolver;

public class TvShowChangeListener extends AbstractVideoChangeListener implements ChangeListener<Boolean> {
    TvShowChangeListener(VideoRow videoRow, CustomTextFieldCell outputCell, OutputResolver outputResolver) {
        super(videoRow, outputCell, outputResolver);
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        videoRow.setIsTvShow(newValue);
        processChange(newValue);
    }
}
