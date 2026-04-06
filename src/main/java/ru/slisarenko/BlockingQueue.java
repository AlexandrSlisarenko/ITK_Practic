package ru.slisarenko;

public interface BlockingQueue <T>{
    void enqueue(T item) throws InterruptedException;
    T dequeue() throws InterruptedException;
    int sizeQueue();
}
