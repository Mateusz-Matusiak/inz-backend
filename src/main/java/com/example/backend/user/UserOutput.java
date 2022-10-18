package com.example.backend.user;

public record UserOutput(Long id,
                         String firstName,
                         String lastName,
                         String email,
                         String password,
                         String phoneNumber,
                         String role) {
}
