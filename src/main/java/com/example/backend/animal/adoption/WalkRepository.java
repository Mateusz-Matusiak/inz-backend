package com.example.backend.animal.adoption;

import com.example.backend.animal.AnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WalkRepository extends JpaRepository<WalkEntity, Long> {
    List<WalkEntity> findAllByDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    long countAllByAnimalAndDateBetween(AnimalEntity animal, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
