package com.example.backend.exception;


import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceAlreadyExistsException.class})
    public ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ExceptionRequestBodyDTO(Map.of("error", ex.getMessage())),
                new HttpHeaders(),
                CONFLICT, request);
    }

    @ExceptionHandler(value = {ResourceNotExistsException.class})
    public ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ExceptionRequestBodyDTO(Map.of("error", ex.getMessage())),
                new HttpHeaders(),
                NOT_FOUND,
                request);
    }
}
