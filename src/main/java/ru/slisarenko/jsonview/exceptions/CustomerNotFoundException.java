package ru.slisarenko.jsonview.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super(String.format("Customer with id = %s not found!", id.toString()));
    }
}
