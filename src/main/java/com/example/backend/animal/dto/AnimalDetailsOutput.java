package com.example.backend.animal.dto;

import com.example.backend.animal.details.AnimalSex;

import java.time.LocalDate;
import java.util.Set;

public record AnimalDetailsOutput(
        Long id,
        String name,
        Integer age,
        LocalDate catchDate,
        Set<String> imagesPaths,

        String type,
        String ownerEmail,
        String color,
        String character,
        String description,
        AnimalSex sex,
        Integer size
) {
}
