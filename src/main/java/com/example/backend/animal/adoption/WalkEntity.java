package com.example.backend.animal.adoption;

import com.example.backend.animal.AnimalEntity;
import com.example.backend.user.UserEntity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "walks")
public class WalkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    private AnimalEntity animal;
    private LocalDateTime date;

    public WalkEntity(UserEntity user, AnimalEntity animal, LocalDateTime date) {
        this.user = user;
        this.animal = animal;
        this.date = date;
    }

    public WalkEntity() {

    }
}
