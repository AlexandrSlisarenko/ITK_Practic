package ru.slisarenko;

import java.util.concurrent.RecursiveTask;

public class FactorialTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 5;
    private int start;
    private int end;

    public FactorialTask(int n) {
        this (1, n);

    }

    private FactorialTask(int start, int end) {
        this.end = end;
        this.start = start;
    }

    @Override
    protected Integer compute() {
        int length = end - start + 1;
        if (length <= THRESHOLD) {
            int number = 1;
            for (int i = start; i <= end; i++) {
                number *= i;
            }
            return number;
        } else {
            int mid = (this.start + this.end) / 2;
            FactorialTask leftTask = new FactorialTask(start, mid);
            FactorialTask rightTask = new FactorialTask(mid + 1, end);

            leftTask.fork();
            rightTask.fork();
            int rightResult = rightTask.join();
            int leftResult = leftTask.join();

            return leftResult * rightResult;
        }

    }
}
