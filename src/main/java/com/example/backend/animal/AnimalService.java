package com.example.backend.animal;

import com.example.backend.animal.details.AnimalDetailsEntity;
import com.example.backend.animal.details.AnimalDetailsRepository;
import com.example.backend.animal.dto.AnimalDetailsOutput;
import com.example.backend.animal.dto.AnimalOutput;
import com.example.backend.animal.dto.NewAnimalDTO;
import com.example.backend.animal.type.AnimalTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final AnimalDetailsRepository animalDetailsRepository;
    private final AnimalTypeRepository animalTypeRepository;

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
                        animal -> new AnimalOutput(animal.getId(), animal.getName(),
                                animal.getAge(), animal.getShelterDate(),
                                animal.getAnimalTypeEntity().getTypeName(),
                                animal.getAnimalDetailsEntity().getSex(),
                                animal.getAnimalDetailsEntity().getSize()));
    }

    public List<AnimalOutput> getAllAnimals() {
        return animalRepository.findAll().stream()
                .map(
                        animalEntity -> new AnimalOutput(animalEntity.getId(), animalEntity.getName(),
                                animalEntity.getAge(), animalEntity.getShelterDate(),
                                animalEntity.getAnimalTypeEntity().getTypeName(),
                                animalEntity.getAnimalDetailsEntity().getSex(),
                                animalEntity.getAnimalDetailsEntity().getSize()))
                .toList();
    }

    public Optional<AnimalDetailsOutput> getAnimalDetailsById(Long id) {
        return animalRepository.findById(id).map(
                animal ->
                        new AnimalDetailsOutput(
                                animal.getId(), animal.getName(), animal.getAge(),
                                animal.getShelterDate(), animal.getAnimalTypeEntity().getTypeName(),
                                animal.getOwner() == null ? null : animal.getOwner().getEmail(), animal.getAnimalDetailsEntity().getColor(),
                                animal.getAnimalDetailsEntity().getCharacter(), animal.getAnimalDetailsEntity().getDescription(),
                                animal.getAnimalDetailsEntity().getSex(), animal.getAnimalDetailsEntity().getSize())
        );
    }
}
