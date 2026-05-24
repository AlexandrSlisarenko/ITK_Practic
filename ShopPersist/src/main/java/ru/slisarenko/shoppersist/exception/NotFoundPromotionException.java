package ru.slisarenko.shoppersist.exception;

public class NotFoundPromotionException extends RuntimeException {
    public NotFoundPromotionException(String message) {
        super(message);
    }
}
