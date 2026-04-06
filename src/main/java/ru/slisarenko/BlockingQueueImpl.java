package ru.slisarenko;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueueImpl implements BlockingQueue<Message> {
    private Queue<Message> queue;
    private int capacity;

    public BlockingQueueImpl(int capacity) {
        if (capacity < 1){
            throw new IllegalArgumentException("capacity must be greater than 0");
        }
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    @Override
    public synchronized void enqueue(Message item) throws InterruptedException {
        this.queue.add(item);
        if (this.queue.size() + 1 == this.capacity){
            this.wait();
        }
        this.notify();
    }

    @Override
    public synchronized Message dequeue() throws InterruptedException {
        if(this.queue.isEmpty()){
            this.wait();
        }
        var result = this.queue.poll();
        this.notify();
        return result;
    }

    @Override
    public synchronized int sizeQueue() {
        return this.queue.size();
    }
}
