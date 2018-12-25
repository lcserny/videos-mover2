package net.cserny.videosmover.helper;

public interface LoadingService {

    void showLoading();

    void hideLoading();

    void register(Runnable showLoadingRunnable, Runnable hideLoadingRunnable);
}
