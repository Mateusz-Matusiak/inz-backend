package com.example.backend.animal;

import com.example.backend.animal.adoption.dto.WalkOutput;
import com.example.backend.animal.details.AnimalDetailsEntity;
import com.example.backend.animal.details.AnimalDetailsRepository;
import com.example.backend.animal.dto.AnimalDetailsOutput;
import com.example.backend.animal.dto.AnimalOutput;
import com.example.backend.animal.dto.NewAnimalDTO;
import com.example.backend.animal.images.ImageEntity;
import com.example.backend.animal.images.ImageRepository;
import com.example.backend.animal.type.AnimalTypeEntity;
import com.example.backend.animal.type.AnimalTypeRepository;
import com.example.backend.exception.ResourceNotExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final AnimalDetailsRepository animalDetailsRepository;
    private final AnimalTypeRepository animalTypeRepository;
    private final ImageRepository imageRepository;

    public Optional<AnimalOutput> addAnimal(NewAnimalDTO newAnimal) {
        return animalTypeRepository.findByTypeName(newAnimal.animalType())
                .map(animalTypeEntity -> {
                    AnimalDetailsEntity newAnimalDetails = animalDetailsRepository.save(
                            new AnimalDetailsEntity(newAnimal.colour(), newAnimal.character(),
                                    newAnimal.description(), newAnimal.sex(), newAnimal.size()));
                    return animalRepository.save(
                            new AnimalEntity(newAnimal.name(), newAnimal.age(),
                                    LocalDate.now(), "/",
                                    animalTypeEntity, newAnimalDetails));
                }).map(

                        animal -> imageRepository.findByAnimalAndMainIsTrue(animal)
                                .map(image -> new AnimalOutput(animal.getId(), animal.getName(),
                                        animal.getAge(), animal.getShelterDate(),
                                        image.getFilePath(),
                                        animal.getAnimalTypeEntity().getTypeName(),
                                        animal.getAnimalDetailsEntity().getSex(),
                                        animal.getAnimalDetailsEntity().getSize()))
                                .orElse(new AnimalOutput(animal.getId(), animal.getName(),
                                        animal.getAge(), animal.getShelterDate(),
                                        null, animal.getAnimalTypeEntity().getTypeName(),
                                        animal.getAnimalDetailsEntity().getSex(),
                                        animal.getAnimalDetailsEntity().getSize()))
                );
    }

    public List<AnimalOutput> getAllAnimals() {
        return animalRepository.findAll().stream()
                .map(
                        animalEntity -> imageRepository.findByAnimalAndMainIsTrue(animalEntity)
                                .map(image -> new AnimalOutput(animalEntity.getId(), animalEntity.getName(),
                                        animalEntity.getAge(),
                                        animalEntity.getShelterDate(),
                                        image.getFilePath(),
                                        animalEntity.getAnimalTypeEntity().getTypeName(),
                                        animalEntity.getAnimalDetailsEntity().getSex(),
                                        animalEntity.getAnimalDetailsEntity().getSize())
                                )
                                .orElse(new AnimalOutput(animalEntity.getId(), animalEntity.getName(),
                                        animalEntity.getAge(), animalEntity.getShelterDate(),
                                        null,
                                        animalEntity.getAnimalTypeEntity().getTypeName(),
                                        animalEntity.getAnimalDetailsEntity().getSex(),
                                        animalEntity.getAnimalDetailsEntity().getSize()))
                )
                .toList();
    }

    public Optional<AnimalDetailsOutput> getAnimalDetailsById(Long id) {

        return animalRepository.findById(id).map(
                animal ->
                        new AnimalDetailsOutput(
                                animal.getId(), animal.getName(), animal.getAge(),
                                animal.getShelterDate(),
                                imageRepository.findAllByAnimal(animal).stream().map(ImageEntity::getId).collect(Collectors.toSet()),
                                animal.getAnimalTypeEntity().getTypeName(),
                                animal.getOwner() == null ? null : animal.getOwner().getEmail(), animal.getAnimalDetailsEntity().getColor(),
                                animal.getAnimalDetailsEntity().getCharacter(), animal.getAnimalDetailsEntity().getDescription(),
                                animal.getAnimalDetailsEntity().getSex(), animal.getAnimalDetailsEntity().getSize())
        );
    }

    public List<WalkOutput> getIncomingWalksById(Long id) {
        return animalRepository.findById(id).map(animal ->
                animal.getWalks().stream().filter(walkEntity -> LocalDateTime.now().isBefore(walkEntity.getDate())).map(walk -> new WalkOutput(walk.getId(), walk.getDate())).toList()
        ).orElseThrow(() -> new ResourceNotExistsException("Animal does not exist"));
    }

    @Transactional
    public void updateAnimalById(Long id, NewAnimalDTO animal) {
        final Optional<AnimalTypeEntity> typeOptional = animalTypeRepository.findByTypeName(animal.animalType());
        if (typeOptional.isEmpty()) {
            throw new ResourceNotExistsException(String.format("%s type not found", animal.animalType()));
        }
        final AnimalTypeEntity typeEntity = typeOptional.get();

        final Optional<AnimalEntity> animalOptional = animalRepository.findById(id);
        if (animalOptional.isEmpty()) {
            throw new ResourceNotExistsException(String.format("Animal with id %d not found", id));
        }
        final AnimalEntity animalEntity = animalOptional.get();
        animalEntity.setAnimalTypeEntity(typeEntity);
        animalEntity.setAge(animal.age());
        animalEntity.setName(animal.name());
        final AnimalDetailsEntity animalDetails = animalEntity.getAnimalDetailsEntity();
        animalDetails.setCharacter(animal.character());
        animalDetails.setSize(animal.size());
        animalDetails.setSex(animal.sex());
        animalDetails.setDescription(animal.description());
        animalDetails.setColor(animal.colour());
        final AnimalDetailsEntity savedDetails = animalDetailsRepository.save(animalDetails);
        animalEntity.setAnimalDetailsEntity(savedDetails);
        animalRepository.save(animalEntity);
    }
}
