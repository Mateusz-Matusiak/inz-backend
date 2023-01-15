package com.example.backend.animal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalRepository extends JpaRepository<AnimalEntity, Long> {
    Optional<AnimalEntity> findByIdAndOwnerIsNull(Long aLong);
}
