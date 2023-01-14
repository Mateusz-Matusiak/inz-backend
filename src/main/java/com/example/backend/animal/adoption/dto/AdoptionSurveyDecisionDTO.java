package com.example.backend.animal.adoption.dto;

import javax.validation.constraints.NotNull;

public record AdoptionSurveyDecisionDTO(
        @NotNull Long id,
        @NotNull Boolean decision,
        @NotNull String message
) {
}
