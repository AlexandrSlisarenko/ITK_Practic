package ru.slisarenko.persist.exception;

public class KafkaError extends RuntimeException {
    public KafkaError(String message) {
        super(message);
    }
}
