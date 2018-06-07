package cc.lasmgratel.lasmutil.thread;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class ThreadPoolManager {
    private final Map<String, ExecutorService> executorServiceMap = new ConcurrentHashMap<>();
    private final Map<String, Supplier<ExecutorService>> supplierMap = new ConcurrentHashMap<>();

    public ThreadPoolExecutor allocateFixedPool(String id, int threads) {
        if (!supplierMap.containsKey(id))
            supplierMap.put(id, () -> Executors.newFixedThreadPool(threads));
        return (ThreadPoolExecutor) executorServiceMap.put(id, supplierMap.get(id).get());
    }

    public ThreadPoolExecutor allocateFixedPool(int threads) {
        return allocateFixedPool(UUID.randomUUID().toString(), threads);
    }

    public ThreadPoolExecutor allocateCachedPool(String id) {
        if (!supplierMap.containsKey(id))
            supplierMap.put(id, Executors::newCachedThreadPool);
        return (ThreadPoolExecutor) executorServiceMap.put(id, supplierMap.get(id).get());
    }

    public ThreadPoolExecutor allocateCachedPool() {
        return allocateCachedPool(UUID.randomUUID().toString());
    }

    public <T> CompletableFuture<T> submitAsync(String id, Supplier<T> supplier) {
        return executorServiceMap.containsKey(id) ?
            CompletableFuture.supplyAsync(supplier, executorServiceMap.get(id)) :
            CompletableFuture.supplyAsync(supplier);
    }

    public void execute(String id, Runnable runnable) {
        if (executorServiceMap.containsKey(id))
            executorServiceMap.get(id).execute(runnable);
    }

    public ExecutorService getExecutorService(String id) {
        return executorServiceMap.get(id);
    }

    public void shutdown(String id) {
        if (executorServiceMap.containsKey(id))
            executorServiceMap.get(id).shutdown();
    }

    public void awaitTerminate(String id) {
        if (executorServiceMap.containsKey(id)) {
            ExecutorService executorService = executorServiceMap.get(id);
            while (!executorService.isTerminated()) {}
        }
    }

    public void awaitForTime(String id, long time, TimeUnit unit) {
        if (executorServiceMap.containsKey(id)) {
            try {
                executorServiceMap.get(id).awaitTermination(time, unit);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdownAndAwait(String id) {
        shutdown(id);
        awaitTerminate(id);
    }

    public void shutdownAndAwaitForTime(String id, long time, TimeUnit unit) {
        shutdown(id);
        awaitForTime(id, time, unit);
    }

    public void restart(String id) {
        if (executorServiceMap.containsKey(id) && supplierMap.containsKey(id))
            executorServiceMap.put(id, supplierMap.get(id).get());
    }

    public void shutdownAwaitAndRestart(String id) {
        shutdownAndAwait(id);
        restart(id);
    }

    public void shutdownAwaitForTimeAndRestart(String id, long time, TimeUnit unit) {
        shutdownAndAwaitForTime(id, time, unit);
        restart(id);
    }

    public void forceRestart(String id) {
        if (executorServiceMap.containsKey(id))
            executorServiceMap.get(id).shutdownNow();
        restart(id);
    }

    @Override
    public String toString() {
        return executorServiceMap.toString();
    }
}
