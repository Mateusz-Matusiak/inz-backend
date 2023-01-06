package com.example.backend.user.dto;

public record UserWithAddressOutput(
        Long id, String email, String name, String phone, String address
) {
}
