package com.example.backend.animal.type;

import com.example.backend.animal.dto.NewAnimalTypeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimalTypeService {

    private final AnimalTypeRepository animalTypeRepository;

    public List<AnimalTypeEntity> getAllAnimalTypes() {
        return animalTypeRepository.findAll();
    }

    public Optional<AnimalTypeEntity> addNewAnimalType(NewAnimalTypeDTO newAnimalType) {
        if (animalTypeRepository.findByTypeName(newAnimalType.name()).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(animalTypeRepository.save(new AnimalTypeEntity(newAnimalType.name())));
    }
}
