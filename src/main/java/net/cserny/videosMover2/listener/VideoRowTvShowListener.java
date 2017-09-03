package net.cserny.videosMover2.listener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import net.cserny.videosMover2.dto.VideoRow;
import net.cserny.videosMover2.service.VideoOutputNameResolver;
import net.cserny.videosMover2.service.VideoOutputNameResolverImpl;

/**
 * Created by leonardo on 03.09.2017.
 */
public class VideoRowTvShowListener implements ChangeListener<Boolean>
{
    private VideoOutputNameResolver nameResolver = new VideoOutputNameResolverImpl();
    private final VideoRow videoRow;

    public VideoRowTvShowListener(VideoRow videoRow) {
        this.videoRow = videoRow;
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        videoRow.setIsTvShow(newValue);
        videoRow.setOutput(newValue ? nameResolver.resolveTvShow(videoRow.getVideo()) : "");
    }
}
