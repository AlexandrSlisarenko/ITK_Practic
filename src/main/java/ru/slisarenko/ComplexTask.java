package ru.slisarenko;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class ComplexTask implements IComplexTask<Integer>{
    private final Integer numberTask;
    private int duration;

    public ComplexTask(Integer numberTask) {
        this.numberTask = numberTask;
    }

    @Override
    public Integer execute() {
        try{
            this.duration = ThreadLocalRandom.current().nextInt(500, 5000);
            Thread.sleep(duration);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(Thread.currentThread().getName() + " выполнил задачу " +
                           this.numberTask + " -> время выполнения: " + this.duration);
        return this.duration;

    }
}
