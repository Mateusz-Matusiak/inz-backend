package com.example.backend.animal.images;

public record SavedImageDTO(long id, String path,
                            boolean main, String type,
                            long animalId) {
}
