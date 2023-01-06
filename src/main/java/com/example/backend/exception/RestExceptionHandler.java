package com.example.backend.exception;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceAlreadyExistsException.class, IncorrectProviderException.class})
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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(
                error -> errors.put(((FieldError) error).getField(),
                        error.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(new ExceptionRequestBodyDTO(errors));
    }
}
