package com.example.backend.animal.adoption;

import com.example.backend.animal.adoption.dto.WalkDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;

@RestController
@RequiredArgsConstructor
public class AdoptionController {

    private final WalkService walkService;

    @PostMapping("/walks")
    public ResponseEntity<WalkDTO> addWalk(@RequestBody WalkDTO walk) {
        return walkService.reserveWalkWithDog(walk.userId(), walk.animalId(), walk.date())
                .map(walkEntity -> ResponseEntity.ok(
                        new WalkDTO(walkEntity.getId(), walkEntity.getAnimal().getId(),
                                walkEntity.getDate())))
                .orElse(ResponseEntity.status(I_AM_A_TEAPOT).build());
    }

    @GetMapping("/walks/today")
    public ResponseEntity<List<WalkEntity>> getTodayWalks() {
        return ResponseEntity.ok(walkService.getAllWalksToday());
    }
}
