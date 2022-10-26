package com.example.backend.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public record RegisterUserDTO(@Email String email,
                              @Size(min = 7) String password) {
}
