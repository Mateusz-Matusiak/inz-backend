package com.example.backend.animal;

import com.example.backend.animal.adoption.AdoptionSurveyEntity;
import com.example.backend.animal.details.AnimalDetailsEntity;
import com.example.backend.animal.type.AnimalTypeEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "animals")
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    private LocalDate shelterDate;
    private String imagePath;
    @ManyToOne(targetEntity = AnimalTypeEntity.class)
    @JoinColumn(name = "animal_type_id")
    private AnimalTypeEntity animalTypeEntity;

    @OneToOne(targetEntity = AnimalDetailsEntity.class)
    @JoinColumn(name = "animal_detail_id")
    private AnimalDetailsEntity animalDetailsEntity;
    @OneToMany(mappedBy = "animal")
    private List<AdoptionSurveyEntity> adoptionSurveys;

    public AnimalEntity(String name, Integer age, LocalDate shelterDate, String imagePath, AnimalTypeEntity animalTypeEntity) {
        this.name = name;
        this.age = age;
        this.shelterDate = shelterDate;
        this.imagePath = imagePath;
        this.animalTypeEntity = animalTypeEntity;
    }


    public AnimalEntity(String name, Integer age, LocalDate shelterDate, String imagePath, AnimalTypeEntity animalTypeEntity, AnimalDetailsEntity animalDetailsEntity) {
        this.name = name;
        this.age = age;
        this.shelterDate = shelterDate;
        this.imagePath = imagePath;
        this.animalTypeEntity = animalTypeEntity;
        this.animalDetailsEntity = animalDetailsEntity;
    }
}
