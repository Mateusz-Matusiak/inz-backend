package com.example.backend.exception;

public class ResourceNotExistsException extends RuntimeException {

    public ResourceNotExistsException(String message) {
        super(message);
    }
}
