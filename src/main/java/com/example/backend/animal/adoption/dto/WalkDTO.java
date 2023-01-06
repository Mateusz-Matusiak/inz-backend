package com.example.backend.animal.adoption.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record WalkDTO(@NotNull Long animalId,
                      @NotNull LocalDateTime date) {
}
