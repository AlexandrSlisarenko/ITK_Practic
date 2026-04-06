package ru.slisarenko;

import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        var executor = Executors.newFixedThreadPool(5);
        BlockingQueue<Message> queue = new BlockingQueueImpl(5);
        executor.execute(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    queue.enqueue(new Message("Message " + i));
                    System.out.println("Produced: Message " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.execute(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    var message = queue.dequeue();
                    System.out.println("Consumed: " + message.message());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        executor.shutdown();
    }
}