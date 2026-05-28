package ru.slisarenko.persist.exception;

public class NotFoundHandlerIntegrationException extends RuntimeException {
    public NotFoundHandlerIntegrationException(String message) {
        super(message);
    }
}
