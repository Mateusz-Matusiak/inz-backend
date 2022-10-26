package com.example.backend.animal.images;

import com.example.backend.animal.AnimalEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "images")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path;
    @ManyToOne(targetEntity = AnimalEntity.class)
    @JoinColumn(name = "animal_id")
    private AnimalEntity animal;
    private boolean main;

    public ImageEntity(String path, AnimalEntity animal, boolean main) {
        this.path = path;
        this.animal = animal;
        this.main = main;
    }

    public ImageEntity(String path, AnimalEntity animal) {
        this.path = path;
        this.animal = animal;
        this.main = false;
    }
}
