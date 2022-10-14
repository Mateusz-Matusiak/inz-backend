package com.example.backend.animal.type;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "animal_types")
public class AnimalTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String typeName;

    public AnimalTypeEntity(String typeName) {
        this.typeName = typeName;
    }
}
