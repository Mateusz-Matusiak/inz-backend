package com.example.backend.animal.adoption;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionSurveyRepository extends JpaRepository<AdoptionSurveyEntity, Long> {
}
