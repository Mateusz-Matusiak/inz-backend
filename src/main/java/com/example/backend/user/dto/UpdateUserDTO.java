package com.example.backend.user.dto;

public record UpdateUserDTO(
        String firstName,
        String lastName,
        String phoneNumber,
        String role
) {
}
