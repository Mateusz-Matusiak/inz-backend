package com.example.backend.user.dto;

public record UserDetailsOutput(
        Long id, String email, String name, String phone, String city, String streetName,
        Integer streetNumber, String postalCode, String country
) {
}
