package ru.slisarenko.spring_data_projections.service.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFoundProduct(EmployeeNotFoundException ex, WebRequest request) {
        var error = ErrorDetails.builder()
                .details(request.getDescription(false))
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFoundOrder(DepartmentNotFoundException ex, WebRequest request) {
        var error = ErrorDetails.builder()
                .details(request.getDescription(false))
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleGeneric(IllegalArgumentException ex, WebRequest request) {
        ErrorDetails error = ErrorDetails.builder()
                .details(request.getDescription(false))
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGeneric(Exception ex, WebRequest request) {
        ErrorDetails error = ErrorDetails.builder()
                .details(request.getDescription(false))
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
