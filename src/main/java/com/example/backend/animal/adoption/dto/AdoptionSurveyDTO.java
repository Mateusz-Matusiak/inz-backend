package com.example.backend.animal.adoption.dto;

import java.time.LocalDate;

public record AdoptionSurveyDTO(Long id, Long animalId,
                                Long userId, String animalName,
                                LocalDate creationDate,
                                Boolean isAccepted,
                                String message
) {
}
