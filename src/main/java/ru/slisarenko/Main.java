package ru.slisarenko;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int numberOfTasks = 5;
        ComplexTaskExecutor executor = new ComplexTaskExecutor(numberOfTasks);
        executor.executeTasks(numberOfTasks);
    }
}