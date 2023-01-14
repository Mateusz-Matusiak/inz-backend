package com.example.backend.animal.adoption;

import com.example.backend.animal.AnimalEntity;
import com.example.backend.user.UserEntity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

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
    private LocalDate date;

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    private String declineMessage;

    public AdoptionSurveyEntity(UserEntity user, AnimalEntity animal) {
        this.user = user;
        this.animal = animal;
        this.date = LocalDate.now();
    }

    public AdoptionSurveyEntity() {
    }
}
