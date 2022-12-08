package com.example.backend.animal.images;

import com.example.backend.animal.AnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    Optional<ImageEntity> findByFilePath(String filePath);

    long countAllByAnimal(AnimalEntity animal);

    List<ImageEntity> findAllByAnimal(AnimalEntity animal);

    Optional<ImageEntity> findByAnimalAndMainIsTrue(AnimalEntity animal);
}
