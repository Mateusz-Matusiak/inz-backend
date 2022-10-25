package com.example.backend.animal.dto;


public record NewAnimalDTO(
        String name,
        Integer age,
        //images
        String animalType,
        String colour,
        String character,
        String description,
        String sex,
        Integer size
) {
}
