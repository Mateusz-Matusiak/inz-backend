package com.example.backend.animal.adoption;

import com.example.backend.animal.AnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WalkRepository extends JpaRepository<WalkEntity, Long> {

    long countAllByAnimalAndDateBetween(AnimalEntity animal, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<WalkEntity> findAllByAnimal(AnimalEntity animal);

    List<WalkEntity> findAllByUserEmail(String email);

    List<WalkEntity> findAllByUserIdAndDateAfter(Long id, LocalDateTime date);
}
