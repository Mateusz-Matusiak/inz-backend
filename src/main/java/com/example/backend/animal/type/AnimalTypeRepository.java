package com.example.backend.animal.type;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalTypeRepository extends JpaRepository<AnimalTypeEntity, Long> {

    Optional<AnimalTypeEntity> findByTypeName(String typeName);
}
