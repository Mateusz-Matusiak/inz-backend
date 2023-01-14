package com.example.backend.animal.adoption.dto;

import javax.validation.constraints.NotNull;

public record NewAdoptionSurveyDTO(
        @NotNull Long animalId
) {
}
