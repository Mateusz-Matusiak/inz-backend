package com.example.backend.exception;

public class IncorrectProviderException extends RuntimeException {
    public IncorrectProviderException(String message) {
        super(message);
    }
}
