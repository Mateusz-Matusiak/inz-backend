package com.example.backend.animal.dto;

import com.example.backend.animal.details.AnimalSex;

import java.time.LocalDate;

public record AnimalOutput(
        long id,
        String name,
        Integer age,
        LocalDate catchDate,
        //imag
        String type,
        AnimalSex sex,
        Integer size
) {
}
