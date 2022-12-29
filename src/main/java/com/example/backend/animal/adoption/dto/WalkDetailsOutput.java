package com.example.backend.animal.adoption.dto;

import java.time.LocalDateTime;

public record WalkDetailsOutput(Long id,
                                Long animalId, String animalName,
                                String userEmail, LocalDateTime time) {
}
