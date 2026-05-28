package ru.slisarenko.persist.exception;

public class NotFoundServiceIntegrationException extends RuntimeException {
    public NotFoundServiceIntegrationException(String message) {
        super(message);
    }
}
