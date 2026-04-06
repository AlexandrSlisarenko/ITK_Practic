package ru.slisarenko;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ComplexTaskExecutor implements IComplexTaskExecutor{
    private final ExecutorService executor;
    private final CyclicBarrier barrier;
    private final List<Integer> results;

    public ComplexTaskExecutor(int numberOfThreads) {
        this.executor = Executors.newFixedThreadPool(numberOfThreads);
        this.barrier = new CyclicBarrier(numberOfThreads, this::combineResults);
        this.results = Collections.synchronizedList(new ArrayList<>());
    }

    public void combineResults() {
        int total = 0;
        synchronized (results) {
            total = results.stream().mapToInt(Integer::intValue).sum();
        }
        System.out.println("Задача выполнена.Общее время выполнения составило: " + total);
        System.out.println("=========================================\n");
    }

    public void executeTasks(int numberOfTasks) throws InterruptedException {
        if (numberOfTasks <= 0) {
            throw new IllegalArgumentException("Нет задач на выполнение");
        }

        this.results.clear();

        for (int i = 0; i < numberOfTasks; i++) {
            var taskId = i;
            this.executor.execute(() -> {
                try {
                    var task = new ComplexTask(taskId);
                    var result = task.execute();
                    synchronized (this.results) {
                        this.results.add(result);
                    }
                    this.barrier.await();
                }catch (InterruptedException | BrokenBarrierException e){
                    Thread.currentThread().interrupt();
                    System.err.println("Поток " + Thread.currentThread().getName() + " был прерван");
                }
            });
        }
    }

    public void shutdown() {
        this.executor.shutdown();
        try {
            if (!this.executor.awaitTermination(10, TimeUnit.SECONDS)) {
                this.executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            this.executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
