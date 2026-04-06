package ru.slisarenko;

public interface IComplexTaskExecutor {
    public void executeTasks(int numberOfTasks) throws InterruptedException;
    public void combineResults();
    public void shutdown();
}
