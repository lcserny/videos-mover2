package net.cserny.videosmover.core.service.observer;

public interface VideoAdjustmentObserver {
    boolean shouldAdjustPath();

    void dontAdjustPath();
}
