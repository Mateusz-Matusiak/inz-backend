package com.example.backend.animal.images;

import com.example.backend.animal.AnimalEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "images")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filePath;
    @ManyToOne(targetEntity = AnimalEntity.class)
    @JoinColumn(name = "animal_id")
    private AnimalEntity animal;
    private boolean main;
    private String type;

    public ImageEntity(String filePath, AnimalEntity animal, boolean main, String type) {
        this.filePath = filePath;
        this.animal = animal;
        this.main = main;
        this.type = type;
    }

    public ImageEntity(String filePath, AnimalEntity animal, String type) {
        this.filePath = filePath;
        this.animal = animal;
        this.main = false;
        this.type = type;
    }
}
