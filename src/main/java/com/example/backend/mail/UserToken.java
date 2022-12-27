package com.example.backend.mail;

import lombok.Builder;

@Builder
public record UserToken(String email, String token, String callbackUrl) {
}
