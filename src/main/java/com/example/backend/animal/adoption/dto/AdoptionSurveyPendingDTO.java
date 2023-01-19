package com.example.backend.animal.adoption.dto;

import java.time.LocalDate;

public record AdoptionSurveyPendingDTO(
        Long id, Long animalId,
        Long userId, String animalName,
        LocalDate creationDate,
        String userEmail,
        Long walks
) {
}
