package com.example.backend.animal.images;

import com.example.backend.animal.AnimalEntity;
import com.example.backend.animal.AnimalRepository;
import com.example.backend.exception.ResourceNotExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private static final String FOLDER_PATH = "/images";
    private final ImageRepository imageRepository;
    private final AnimalRepository animalRepository;
    private final Path rootLocation = Paths.get(FOLDER_PATH);

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ImageEntity> uploadImageForAnimal(MultipartFile file, Long animalId) {
        Optional<AnimalEntity> animalOptional = animalRepository.findById(animalId);
        if (file.isEmpty()) {
            throw new ResourceNotExistsException("Failed to store empty file");
        }
        if (animalOptional.isPresent()) {
            AnimalEntity animal = animalOptional.get();
            String directoryPath = String.format("%s/%d", FOLDER_PATH, animal.getId());
            String filePath = String.format("%s/%s", directoryPath, file.getOriginalFilename());
            try {
                Path destinationDirectory = Files.createDirectories(Path.of(directoryPath));
                Path destinationFile = destinationDirectory.resolve(Paths.get(file.getOriginalFilename()))
                        .normalize().toAbsolutePath();
                if (!destinationFile.getParent().equals(destinationDirectory)) {
                    log.warn("Cannot store file outside current directory");
                    //todo throw
                }
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, destinationFile, REPLACE_EXISTING);
                }
                return Optional.of(imageRepository.save(new ImageEntity(filePath, animal, file.getContentType())));
            } catch (IOException e) {
                log.error("Could not save new file ", e);
            }
        } else {
            throw new ResourceNotExistsException(String.format("Animal with id %d does not exist", animalId));
        }
        return Optional.empty();
    }

}
