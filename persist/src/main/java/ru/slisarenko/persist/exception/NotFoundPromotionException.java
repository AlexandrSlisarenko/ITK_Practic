package ru.slisarenko.persist.exception;

public class NotFoundPromotionException extends RuntimeException {
    public NotFoundPromotionException(String message) {
        super(message);
    }
}
