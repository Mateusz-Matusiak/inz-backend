package com.example.backend.animal.dto;


import com.example.backend.animal.details.AnimalSex;

public record NewAnimalDTO(
        String name,
        Integer age,
        String animalType,
        String colour,
        String character,
        String description,
        AnimalSex sex,
        Integer size
) {
}
