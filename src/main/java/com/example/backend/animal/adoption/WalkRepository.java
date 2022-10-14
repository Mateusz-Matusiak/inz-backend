package com.example.backend.animal.adoption;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkRepository extends JpaRepository<WalkEntity, Long> {
}
