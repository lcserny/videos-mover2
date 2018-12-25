package net.cserny.videosmover.service.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TwoThreadsExecutor {

    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void doInAnotherThread(Runnable runnable) {
        try {
            executorService.execute(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }
}
