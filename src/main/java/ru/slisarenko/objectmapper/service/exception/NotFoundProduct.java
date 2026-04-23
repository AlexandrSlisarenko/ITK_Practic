package ru.slisarenko.objectmapper.service.exception;

public class NotFoundProduct extends RuntimeException {
    public NotFoundProduct(Long id) {
        super(String.format("Product with id = %s not found!", id.toString()));
    }
}
