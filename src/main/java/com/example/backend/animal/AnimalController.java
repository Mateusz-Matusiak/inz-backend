package com.example.backend.animal;

import com.example.backend.animal.adoption.dto.WalkOutput;
import com.example.backend.animal.dto.AnimalDetailsOutput;
import com.example.backend.animal.dto.AnimalOutput;
import com.example.backend.animal.dto.NewAnimalDTO;
import com.example.backend.animal.dto.NewAnimalTypeDTO;
import com.example.backend.animal.images.ImageService;
import com.example.backend.animal.images.SavedImageDTO;
import com.example.backend.animal.type.AnimalTypeEntity;
import com.example.backend.animal.type.AnimalTypeService;
import com.example.backend.exception.ResourceNotExistsException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/animals")
@RequiredArgsConstructor
public class AnimalController {
    private final AnimalService animalService;
    private final AnimalTypeService animalTypeService;
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<AnimalOutput> addAnimal(@RequestBody NewAnimalDTO newAnimal) {
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
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<List<SavedImageDTO>> insertAnimalImage(@RequestParam("image") MultipartFile[] files,
                                                                 @PathVariable Long id) {

        List<SavedImageDTO> savedImages = Arrays.stream(files).map(file ->
                imageService.uploadImageForAnimal(file, id).map(imageEntity ->
                        new SavedImageDTO(imageEntity.getId(), imageEntity.getFilePath(),
                                imageEntity.isMain(), imageEntity.getType(),
                                imageEntity.getAnimal().getId())
                ).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST))).toList();

        return ResponseEntity.ok(savedImages);
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<List<SavedImageDTO>> getImagesForAnimal(@PathVariable Long id) {
        return ResponseEntity.ok(imageService.getAllImagesForAnimal(id));
    }

    @GetMapping("/{id}/images/main")
    public ResponseEntity<byte[]> getMainImage(@PathVariable Long id) throws IOException {
        final HttpHeaders headers = new HttpHeaders();
        InputStream is = imageService.getMainImageForAnimal(id).map(savedImage -> {
            headers.setContentType(savedImage.type().equals("image/jpeg")
                    ? MediaType.IMAGE_JPEG
                    : (savedImage.type().equals("image/png") ? MediaType.IMAGE_PNG : MediaType.ALL));
            try {
                return new FileInputStream(savedImage.path());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).orElseThrow(
                () -> new ResourceNotExistsException(String.format("This animal [%d] doesn't have image", id))
        );
        return new ResponseEntity<>(IOUtils.toByteArray(is), headers, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/images/{imageId}")
    public ResponseEntity<byte[]> getImageById(@PathVariable Long id, @PathVariable Long imageId) throws IOException {
        final HttpHeaders headers = new HttpHeaders();
        InputStream is = imageService.getImageByPath(id, imageId)
                .map(image -> {
                    headers.setContentType(image.type().equals("image/jpeg")
                            ? MediaType.IMAGE_JPEG
                            : (image.type().equals("image/png") ? MediaType.IMAGE_PNG : MediaType.ALL));
                    try {
                        return new FileInputStream(image.path());
                    } catch (FileNotFoundException e) {
                        throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, e.getMessage());
                    }
                }).orElseThrow(() -> new ResourceNotExistsException(String.format("This animal [%d] doesn't have image", id)));
        return new ResponseEntity<>(IOUtils.toByteArray(is), headers, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/walks")
    public ResponseEntity<List<WalkOutput>> getIncomingWalksByAnimalId(@PathVariable Long id) {
        return ResponseEntity.ok(animalService.getIncomingWalksById(id));
    }


    @PutMapping("/{id}/details")
    public ResponseEntity<Void> updateAnimalDetails(@RequestBody NewAnimalDTO animal, @PathVariable Long id) {
        animalService.updateAnimalById(id, animal);
        return ResponseEntity.noContent().build();
    }

}
