package com.example.backend.animal.images;

import com.example.backend.animal.AnimalEntity;
import com.example.backend.animal.AnimalRepository;
import com.example.backend.exception.ResourceNotExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private static final String FOLDER_PATH = "/images";
    private final ImageRepository imageRepository;
    private final AnimalRepository animalRepository;


    public Optional<ImageEntity> uploadImageForAnimal(MultipartFile file, Long animalId) {
        Optional<AnimalEntity> animalOptional = animalRepository.findById(animalId);
        if (file.isEmpty()) {
            throw new ResourceNotExistsException("Failed to store empty file");
        }
        if (animalOptional.isPresent()) {
            AnimalEntity animal = animalOptional.get();
            String directoryPath = String.format("%s/%d", FOLDER_PATH, animal.getId());
            try {
                Path destinationFile = Files.createDirectories(Path.of(directoryPath))
                        .resolve(Paths.get(file.getOriginalFilename()))
                        .normalize().toAbsolutePath();
                String savedFilename = saveImageInFileSystem(file, destinationFile);

                return imageRepository.countAllByAnimal(animal) <= 0
                        ? Optional.of(imageRepository.save(new ImageEntity(savedFilename, animal, true, file.getContentType())))
                        : Optional.of(imageRepository.save(new ImageEntity(savedFilename, animal, file.getContentType())));
            } catch (IOException e) {
                log.error("Could not save new file ", e);
            }
        } else {
            throw new ResourceNotExistsException(String.format("Animal with id %d does not exist", animalId));
        }
        return Optional.empty();
    }

    private String saveImageInFileSystem(MultipartFile file, Path destination) throws IOException {
        while (Files.exists(destination)) {
            String[] filename = file.getOriginalFilename().split("\\.(?=[^\\.]+$)");
            String randomFilename = filename[0] + UUID.randomUUID();
            String extension = "." + filename[1];
            destination = destination.getParent().resolve(randomFilename + extension);
        }

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(file.getInputStream())) {
            Files.copy(bufferedInputStream, destination);
        } catch (IOException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return destination.toAbsolutePath().toString();
    }

    public List<SavedImageDTO> getAllImagesForAnimal(Long id) {
        return animalRepository.findById(id).map(imageRepository::findAllByAnimal)
                .orElseThrow(() -> new ResourceNotExistsException(String.format("Animal with %d id does not exist", id)))
                .stream()
                .map(image -> new SavedImageDTO(image.getId(), image.getFilePath(), image.isMain(), image.getType(), id))
                .toList();
    }

    public Optional<SavedImageDTO> getMainImageForAnimal(Long id) {
        return animalRepository.findById(id).map(imageRepository::findByAnimalAndMainIsTrue)
                .orElseThrow(() -> new ResourceNotExistsException("Animal with given id does not exist"))
                .map(image -> new SavedImageDTO(image.getId(), image.getFilePath(),
                        image.isMain(), image.getType(),
                        image.getAnimal().getId()));
    }

}
