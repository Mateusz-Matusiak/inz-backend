package com.example.backend.animal.adoption;

import com.example.backend.animal.adoption.dto.WalkDTO;
import com.example.backend.animal.adoption.dto.WalkDetailsOutput;
import com.example.backend.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;

@RestController
@RequiredArgsConstructor
public class AdoptionController {

    private final WalkService walkService;
    private final UserService userService;

    @PostMapping("/walks")
    public ResponseEntity<WalkDTO> addWalk(Principal principal, @RequestBody WalkDTO walk) {
        final Long userId = userService.getUserIdByEmail(principal.getName());
        return walkService.reserveWalkWithDog(userId, walk.animalId(), walk.date())
                .map(walkEntity -> ResponseEntity.ok(
                        new WalkDTO(walkEntity.getAnimal().getId(),
                                walkEntity.getDate())))
                .orElse(ResponseEntity.status(I_AM_A_TEAPOT).build());
    }


    @GetMapping("/walks")
    public ResponseEntity<List<WalkDetailsOutput>> getAllWalks() {
        return ResponseEntity.ok(walkService.getAllWalks());
    }

    @GetMapping("/users/{id}/walks")
    public ResponseEntity<List<WalkDetailsOutput>> getAllWalksByUsedId(@PathVariable Long id) {
        return ResponseEntity.ok(walkService.getAllWalksByUserId(id));
    }

    @GetMapping("/users/walks")
    public ResponseEntity<List<WalkDetailsOutput>> getMyWalks(Principal principal) {
        return ResponseEntity.ok(walkService.getAllWalksByEmail(principal.getName()));
    }

    @Operation(summary = "Delete for user for his walks")
    @DeleteMapping("/users/walks/{id}")
    public ResponseEntity<Void> deleteUsersWalkById(@PathVariable Long id, Principal principal) {
        walkService.deleteUsersWalkById(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete for admin for all walks")
    @DeleteMapping("/walks/{id}")
    public ResponseEntity<Void> deleteWalkById(@PathVariable Long id) {
        walkService.deleteWalkById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get walks for animal to display them")
    @GetMapping("/animals/{id}/walks/details")
    public ResponseEntity<List<WalkDetailsOutput>> getWalksForAnimal(@PathVariable Long id) {
        return ResponseEntity.ok(walkService.getWalksByAnimalId(id));
    }
}
