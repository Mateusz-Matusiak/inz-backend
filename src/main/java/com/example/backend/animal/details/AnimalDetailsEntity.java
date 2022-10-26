package com.example.backend.animal.details;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "animal_details")
public class AnimalDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String color;
    private String character;
    private String description;
    @Enumerated(EnumType.STRING)
    private AnimalSex sex;
    private Integer size;

    public AnimalDetailsEntity(String color, String character, String description, AnimalSex sex, Integer size) {
        this.color = color;
        this.character = character;
        this.description = description;
        this.sex = sex;
        this.size = size;
    }
}
