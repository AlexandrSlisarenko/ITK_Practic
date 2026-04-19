package ru.slisarenko.spring_data_jdbc.exceptions;

public class BookNotfoundException extends RuntimeException {
    public BookNotfoundException(Long id) {
        super(String.format("Book with id %s not found!!!", id));
    }
}
