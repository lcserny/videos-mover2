package net.cserny.videosmover.core.helper;

public interface LoadingService {

    String SCAN_LOADING_KEY = "scanLoading";
    String MOVE_LOADING_KEY = "moveLoading";

    void showLoading(String label);

    void hideLoading(String label);

    void hideAllLoadings();

    void register(String label, Runnable showLoadingRunnable, Runnable hideLoadingRunnable);
}
