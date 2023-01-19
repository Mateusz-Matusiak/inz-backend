package com.example.backend.animal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<AnimalEntity, Long> {
    Optional<AnimalEntity> findByIdAndOwnerIsNull(Long aLong);

    List<AnimalEntity> findAllByOwnerIsNull();
}
