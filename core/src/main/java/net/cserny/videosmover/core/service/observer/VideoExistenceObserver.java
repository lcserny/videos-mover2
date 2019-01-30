package net.cserny.videosmover.core.service.observer;

public class VideoExistenceObserver implements VideoAdjustmentObserver {

    private boolean shouldAdjustPath = true;

    @Override
    public boolean shouldAdjustPath() {
        return shouldAdjustPath;
    }

    @Override
    public void dontAdjustPath() {
        shouldAdjustPath = false;
    }
}
