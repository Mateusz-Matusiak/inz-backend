package com.example.backend.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record GoogleCredentialsDTO(
        @Email @NotNull String email,
        String familyName,
        String givenName
) {
}
