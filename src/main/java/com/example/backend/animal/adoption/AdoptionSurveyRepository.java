package com.example.backend.animal.adoption;

import com.example.backend.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptionSurveyRepository extends JpaRepository<AdoptionSurveyEntity, Long> {

    List<AdoptionSurveyEntity> findAllByIsAcceptedIsNull();

    List<AdoptionSurveyEntity> findAllByUser(UserEntity user);
}
