package net.cserny.videosmover.service.observer;

public interface VideoAdjustmentObserver {
    boolean shouldAdjustPath();

    void dontAdjustPath();
}
