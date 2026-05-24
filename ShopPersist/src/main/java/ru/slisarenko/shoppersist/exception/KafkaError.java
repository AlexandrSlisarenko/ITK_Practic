package ru.slisarenko.shoppersist.exception;

public class KafkaError extends RuntimeException {
    public KafkaError(String message) {
        super(message);
    }
}
