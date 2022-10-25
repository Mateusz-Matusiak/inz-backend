package com.example.backend.animal.dto;

import java.time.LocalDate;

public record AnimalOutput(
        long id,
        String name,
        Integer age,
        LocalDate catchDate,
        //imag
        String type,
        String sex,
        Integer size
) {
}
