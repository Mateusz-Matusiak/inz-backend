package com.example.backend.user.dto;

public record UserOutput(Long id,
                         String firstName,
                         String lastName,
                         String email,
                         String phoneNumber,
                         String role) {
}
