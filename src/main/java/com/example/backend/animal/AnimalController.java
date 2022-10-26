package com.example.backend.animal;

import com.example.backend.animal.dto.AnimalDetailsOutput;
import com.example.backend.animal.dto.AnimalOutput;
import com.example.backend.animal.dto.NewAnimalDTO;
import com.example.backend.animal.dto.NewAnimalTypeDTO;
import com.example.backend.animal.images.ImageEntity;
import com.example.backend.animal.images.ImageService;
import com.example.backend.animal.type.AnimalTypeEntity;
import com.example.backend.animal.type.AnimalTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/animals")
@RequiredArgsConstructor
public class AnimalController {
    private final AnimalService animalService;
    private final AnimalTypeService animalTypeService;
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<AnimalOutput> addAnimal(NewAnimalDTO newAnimal) {
        return animalService.addAnimal(newAnimal)
                .map(
                        animal -> ResponseEntity.created(
                                        URI.create(String.format("/animals/%d", animal.id())))
                                .body(animal)).orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping
    public ResponseEntity<List<AnimalOutput>> getAllAnimals() {
        return ResponseEntity.ok(animalService.getAllAnimals());
    }

    @GetMapping("/types")
    public ResponseEntity<List<AnimalTypeEntity>> getAllAnimalTypes() {
        return ResponseEntity.ok(animalTypeService.getAllAnimalTypes());
    }

    @PostMapping("/types")
    public ResponseEntity<AnimalTypeEntity> addAnimalType(NewAnimalTypeDTO newAnimalType) {
        return animalTypeService.addNewAnimalType(newAnimalType)
                .map(animalType -> ResponseEntity.created(URI.create(String.format("/animal/types/%d", animalType.getId()))).body(animalType))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<AnimalDetailsOutput> getAnimalDetails(@PathVariable Long id) {
        return animalService.getAnimalDetailsById(id)
                .map(animalDetailsOutput -> ResponseEntity.ok(animalDetailsOutput))
                .orElse(ResponseEntity.internalServerError().build());
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<ImageEntity> insertAnimalImage(@RequestParam("image") MultipartFile file,
                                                         @PathVariable Long id) {
        return imageService.uploadImageForAnimal(file, id)
                .map(imageEntity -> ResponseEntity.ok(imageEntity))
                .orElse(ResponseEntity.internalServerError().build());

    }
}
