package ru.slisarenko.objectmapper.service.exception;

public class NotFoundCustomer extends RuntimeException {
    public NotFoundCustomer(String emailOrContactNumber) {
        super(String.format("Customer with email or contact number = %s not found!", emailOrContactNumber));
    }
}
