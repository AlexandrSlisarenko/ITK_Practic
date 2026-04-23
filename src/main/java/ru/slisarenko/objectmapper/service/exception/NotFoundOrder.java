package ru.slisarenko.objectmapper.service.exception;

public class NotFoundOrder extends RuntimeException {
    public NotFoundOrder(Long id) {
        super(String.format("Order with id = %s not found!", id.toString()));
    }
}
