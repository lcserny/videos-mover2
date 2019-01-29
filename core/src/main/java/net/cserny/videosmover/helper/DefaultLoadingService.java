package net.cserny.videosmover.helper;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class DefaultLoadingService implements LoadingService {

    private final Map<String, Set<Runnable>> showLoadingRunnables = new ConcurrentHashMap<>();
    private final Map<String, Set<Runnable>> hideLoadingRunnables = new ConcurrentHashMap<>();

    @Override
    public void showLoading(String label) {
        if (StringHelper.isEmpty(label)) {
            throw new IllegalArgumentException("Label needed when showing the loading animation/s");
        }
        runInternalSet(showLoadingRunnables.get(label));
    }

    @Override
    public void hideLoading(String label) {
        if (StringHelper.isEmpty(label)) {
            throw new IllegalArgumentException("Label needed when hiding the loading animation/s");
        }
        runInternalSet(hideLoadingRunnables.get(label));
    }

    @Override
    public void hideAllLoadings() {
        for (Set<Runnable> runnables : hideLoadingRunnables.values()) {
            runInternalSet(runnables);
        }
    }

    private void runInternalSet(Set<Runnable> runnables) {
        if (runnables != null && !runnables.isEmpty()) {
            for (Runnable runnable : runnables) {
                runnable.run();
            }
        }
    }

    @Override
    public void register(String label, Runnable showLoadingRunnable, Runnable hideLoadingRunnable) {
        showLoadingRunnables.putIfAbsent(label, new CopyOnWriteArraySet<>());
        showLoadingRunnables.get(label).add(showLoadingRunnable);

        hideLoadingRunnables.putIfAbsent(label, new CopyOnWriteArraySet<>());
        hideLoadingRunnables.get(label).add(hideLoadingRunnable);
    }
}
