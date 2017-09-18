package net.cserny.videosMover.listener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import net.cserny.videosMover.model.VideoRow;
import net.cserny.videosMover.service.OutputNameResolver;

public class TvShowChangeListener implements ChangeListener<Boolean> {
    private final VideoRow videoRow;
    private final OutputNameResolver nameResolver;

    public TvShowChangeListener(VideoRow videoRow, OutputNameResolver nameResolver) {
        this.videoRow = videoRow;
        this.nameResolver = nameResolver;
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        videoRow.setIsTvShow(newValue);
        videoRow.setOutput(newValue ? nameResolver.resolve(videoRow.getVideo()) : "");
    }
}
