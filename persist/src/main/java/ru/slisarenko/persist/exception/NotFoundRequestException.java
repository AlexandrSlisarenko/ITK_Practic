package ru.slisarenko.persist.exception;

public class NotFoundRequestException extends RuntimeException {
    public NotFoundRequestException(String message) {
        super(message);
    }
}
