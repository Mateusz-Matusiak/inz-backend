package com.example.backend.animal.adoption;

import com.example.backend.animal.AnimalEntity;
import com.example.backend.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WalkRepository extends JpaRepository<WalkEntity, Long> {

    long countAllByAnimalAndDateBetween(AnimalEntity animal, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<WalkEntity> findAllByAnimalAndDateBefore(AnimalEntity animal, LocalDateTime date);
    List<WalkEntity> findAllByAnimal(AnimalEntity animal);

    List<WalkEntity> findAllByUserEmail(String email);

    List<WalkEntity> findAllByUserId(Long id);

    int countByUserAndAnimalAndDateBetween(UserEntity user, AnimalEntity animal, LocalDateTime twoMonthsAgo, LocalDateTime today);

    List<WalkEntity> findAllByDateBetween(LocalDateTime now, LocalDateTime tomorrow);
}
