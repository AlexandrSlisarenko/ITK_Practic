package ru.slisarenko.shipping.exception;

public class KafkaException extends RuntimeException {
    public KafkaException(String message) {
        super(message);
    }
}
