package com.example.backend.animal.adoption;

import com.example.backend.animal.AnimalEntity;
import com.example.backend.user.UserEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "adoption_surveys")
public class AdoptionSurveyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne(targetEntity = AnimalEntity.class)
    @JoinColumn(name = "animal_id")
    private AnimalEntity animal;
    private boolean isAccepted;
    private int numberOfWalks;

    public AdoptionSurveyEntity(UserEntity user, AnimalEntity animal) {
        this.user = user;
        this.animal = animal;
        this.isAccepted = false;
    }

    public AdoptionSurveyEntity() {

    }
}
