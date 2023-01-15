package com.example.backend.animal.adoption;

import com.example.backend.animal.AnimalEntity;
import com.example.backend.animal.AnimalRepository;
import com.example.backend.animal.adoption.dto.AdoptionSurveyDTO;
import com.example.backend.exception.ResourceNotExistsException;
import com.example.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdoptionService {

    private static final int WALKS_PERIOD_COUNT = 3;
    private static final int MIN_NUMBER_OF_WALKS_IN_PERIOD = 2;
    private final AdoptionSurveyRepository adoptionRepository;
    private final WalkRepository walkRepository;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;


    @Transactional
    public Optional<AdoptionSurveyDTO> createAdoptionSurvey(String email, Long animalId) {
        final Optional<AnimalEntity> animalOptional = animalRepository.findByIdAndOwnerIsNull(animalId);
        if (animalOptional.isEmpty())
            throw new ResourceNotExistsException("Animal with given id does not exist or has owner");

        final AnimalEntity animalEntity = animalOptional.get();

        return userRepository.findByEmail(email).flatMap(user -> {
            if (walkRepository.countByUserAndAnimalAndDateBetween(user, animalEntity, LocalDateTime.now().minusMonths(WALKS_PERIOD_COUNT), LocalDateTime.now()) < MIN_NUMBER_OF_WALKS_IN_PERIOD) {
                return Optional.empty();
            }
            final AdoptionSurveyEntity createdSurvey = adoptionRepository.save(new AdoptionSurveyEntity(user, animalEntity));
            return Optional.of(new AdoptionSurveyDTO(createdSurvey.getId(), createdSurvey.getAnimal().getId(),
                    createdSurvey.getUser().getId(), createdSurvey.getAnimal().getName(), createdSurvey.getDate(), createdSurvey.getIsAccepted(), createdSurvey.getDeclineMessage()));
        });
    }

    public List<AdoptionSurveyDTO> fetchPendingAdoptionSurveys() {
        return adoptionRepository.findAllByIsAcceptedIsNull().stream()
                .map(adoptionSurveyEntity -> new AdoptionSurveyDTO(
                        adoptionSurveyEntity.getId(), adoptionSurveyEntity.getAnimal().getId(),
                        adoptionSurveyEntity.getUser().getId(), adoptionSurveyEntity.getAnimal().getName(),
                        adoptionSurveyEntity.getDate(), adoptionSurveyEntity.getIsAccepted(), adoptionSurveyEntity.getDeclineMessage())).toList();
    }

    public List<AdoptionSurveyDTO> fetchAdoptionSurveysByUserEmail(String email) {
        return userRepository.findByEmail(email).map(user -> adoptionRepository.findAllByUser(user)
                .stream().map(
                        adoptionSurveyEntity -> new AdoptionSurveyDTO(adoptionSurveyEntity.getId(), adoptionSurveyEntity.getAnimal().getId(), adoptionSurveyEntity.getUser().getId(),
                                adoptionSurveyEntity.getAnimal().getName(), adoptionSurveyEntity.getDate(), adoptionSurveyEntity.getIsAccepted(), adoptionSurveyEntity.getDeclineMessage())
                ).toList()
        ).orElseThrow(
                () -> new ResourceNotExistsException("User with given email does not exist")
        );
    }

    @Transactional
    public void makeUpDecision(Long surveyId, boolean isAccepted, String message) {
        adoptionRepository.findById(surveyId)
                .ifPresentOrElse(
                        adoptionSurveyEntity -> {
                            if (!isAccepted)
                                adoptionSurveyEntity.setDeclineMessage(message);
                            else
                                adoptionSurveyEntity.getAnimal().setOwner(adoptionSurveyEntity.getUser());
                            adoptionSurveyEntity.setIsAccepted(isAccepted);
                        },
                        () -> {
                            throw new ResourceNotExistsException("Adoption survey with given id does not exist");
                        });
    }
}
