package ru.slisarenko.objectmapper.service.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundProduct.class)
    public ResponseEntity<ErrorDetails> handleNotFoundProduct(NotFoundProduct ex, WebRequest request) {
        var error = ErrorDetails.builder()
                .details(request.getDescription(false))
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundOrder.class)
    public ResponseEntity<ErrorDetails> handleNotFoundOrder(NotFoundProduct ex, WebRequest request) {
        var error = ErrorDetails.builder()
                .details(request.getDescription(false))
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundCustomer.class)
    public ResponseEntity<ErrorDetails> handleNotFoundCustomer(NotFoundProduct ex, WebRequest request) {
        var error = ErrorDetails.builder()
                .details(request.getDescription(false))
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorDetails> handleJsonProcessingException(JsonProcessingException ex) {
        var error = ErrorDetails.builder()
                .details(Arrays.toString(ex.getStackTrace()))
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGeneric(Exception ex, WebRequest request) {
        ErrorDetails error = new ErrorDetails(LocalDateTime.now(), "Internal server error", request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
