package ru.slisarenko.spring_security_jwt.exception;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFoundProduct(UsernameNotFoundException ex, WebRequest request) {
        var error = ErrorDetails.builder()
                .details(request.getDescription(false))
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGeneric(Exception ex, WebRequest request) {
        var error = ErrorDetails.builder()
                .message("Internal server error =>" + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .details(Arrays.toString(ex.getStackTrace()))
                .url(request.getDescription(false))
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
