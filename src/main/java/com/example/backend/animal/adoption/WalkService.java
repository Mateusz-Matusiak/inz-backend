package com.example.backend.animal.adoption;

import com.example.backend.animal.AnimalRepository;
import com.example.backend.animal.adoption.dto.WalkDetailsOutput;
import com.example.backend.exception.ResourceNotExistsException;
import com.example.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class WalkService {

    private final WalkRepository walkRepository;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;

    @Transactional
    public Optional<WalkEntity> reserveWalkWithDog(Long userId, Long animalId, LocalDateTime date) {
        return Optional.ofNullable(userRepository.findById(userId).map(user -> animalRepository.findById(animalId).map(animal -> {
            if (walkRepository.countAllByAnimalAndDateBetween(animal, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0), LocalDateTime.now().withHour(23).withMinute(59).withSecond(59)) >
                    2) {
                return null;
            }
            return walkRepository.save(new WalkEntity(user, animal, date));
        }).orElseThrow(() -> {
            final String message = String.format("Animal with id %d does not exist", animalId);
            log.warn(message);
            throw new ResourceNotExistsException(message);
        })).orElseThrow(() -> {
            final String message = String.format("User with id %d does not exist", userId);
            log.warn(message);
            throw new ResourceNotExistsException(message);
        }));
    }

    public List<WalkDetailsOutput> getAllWalks() {
        return walkRepository.findAll().stream().map(
                walk -> new WalkDetailsOutput(walk.getId(), walk.getAnimal().getId(), walk.getAnimal().getName(), walk.getUser().getEmail(), walk.getDate())).toList();
    }

    public List<WalkDetailsOutput> getAllWalksByUserId(Long id) {
        return walkRepository.findAllByUserIdAndDateAfter(id, LocalDateTime.now()).stream().map(
                walkEntity -> new WalkDetailsOutput(walkEntity.getId(), walkEntity.getAnimal().getId(), walkEntity.getAnimal().getName(), walkEntity.getUser().getEmail(),
                        walkEntity.getDate())).toList();
    }

    public List<WalkDetailsOutput> getAllWalksByEmail(String email) {
        return walkRepository.findAllByUserEmail(email).stream().map(
                walkEntity -> new WalkDetailsOutput(walkEntity.getId(), walkEntity.getAnimal().getId(), walkEntity.getAnimal().getName(), walkEntity.getUser().getEmail(),
                        walkEntity.getDate())).toList();
    }

    public void deleteWalkById(Long id) {
        walkRepository.deleteById(id);
    }

    public void deleteUsersWalkById(Long id, String email) {
        walkRepository.findById(id).ifPresentOrElse(walkEntity -> {
            if (Objects.equals(walkEntity.getUser().getEmail(), email)) {
                walkRepository.delete(walkEntity);
            } else {
                throw new ResourceNotExistsException("This walk does not exist for this user");
            }
        }, () -> {
            throw new ResourceNotExistsException("This walk does not exist");
        });
    }


    @Transactional
    public List<WalkDetailsOutput> getWalksByAnimalId(Long id) {
        return animalRepository.findById(id)
                .map(animal -> walkRepository.findAllByAnimal(animal).stream().map(
                        walkEntity -> new WalkDetailsOutput(walkEntity.getId(), walkEntity.getAnimal().getId(), walkEntity.getAnimal().getName(), walkEntity.getUser().getEmail(),
                                walkEntity.getDate())).toList())
                .orElseThrow(() -> new ResourceNotExistsException("Animal with given id does not exist"));
    }

}
