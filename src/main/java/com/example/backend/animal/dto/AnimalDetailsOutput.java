package com.example.backend.animal.dto;

import com.example.backend.animal.details.AnimalSex;

import java.time.LocalDate;

public record AnimalDetailsOutput(
        Long id,
        String name,
        Integer age,
        LocalDate catchDate,
        //todo images
        String type,
        String ownerEmail,
        String color,
        String character,
        String description,
        AnimalSex sex,
        Integer size
) {
}
