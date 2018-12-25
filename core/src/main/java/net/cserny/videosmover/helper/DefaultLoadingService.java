package net.cserny.videosmover.helper;

import com.google.inject.Singleton;

import java.util.HashSet;
import java.util.Set;

@Singleton
public class DefaultLoadingService implements LoadingService {

    private final Set<Runnable> showLoadingRunnables = new HashSet<>();
    private final Set<Runnable> hideLoadingRunnables = new HashSet<>();

    @Override
    public void showLoading() {
        for (Runnable showLoadingRunnable : showLoadingRunnables) {
            showLoadingRunnable.run();
        }
    }

    @Override
    public void hideLoading() {
        for (Runnable hideLoadingRunnable : hideLoadingRunnables) {
            hideLoadingRunnable.run();
        }
    }

    @Override
    public void register(Runnable showLoadingRunnable, Runnable hideLoadingRunnable) {
        showLoadingRunnables.add(showLoadingRunnable);
        hideLoadingRunnables.add(hideLoadingRunnable);
    }
}
