package com.example.backend.exception;

import java.util.Map;

public record ExceptionRequestBodyDTO(Map<String, String> errors) {
}
