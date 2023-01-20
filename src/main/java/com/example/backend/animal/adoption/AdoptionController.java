package com.example.backend.animal.adoption;

import com.example.backend.animal.adoption.dto.*;
import com.example.backend.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;

@RestController
@RequiredArgsConstructor
public class AdoptionController {

    private final WalkService walkService;
    private final UserService userService;
    private final AdoptionService adoptionService;

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

    @Operation(summary = "Make adoption survey to be accepted or not by ADMIN")
    @PostMapping("/adoption-surveys")
    public ResponseEntity<AdoptionSurveyDTO> createAdoptionSurvey(@RequestBody @Valid NewAdoptionSurveyDTO newAdoptionSurvey, Principal principal) {
        return adoptionService.createAdoptionSurvey(principal.getName(), newAdoptionSurvey.animalId())
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST));
    }

    @Operation(summary = "Get all adoption surveys that are not decided yet")
    @GetMapping("/adoption-surveys/pending")
    public ResponseEntity<List<AdoptionSurveyPendingDTO>> getAllAdoptionSurveys() {
        return ResponseEntity.ok(adoptionService.fetchPendingAdoptionSurveys());
    }

    @Operation(summary = "Get logged in user adoption surveys")
    @GetMapping("/adoption-surveys")
    public ResponseEntity<List<AdoptionSurveyDTO>> getMyAdoptionSurveys(Principal principal) {
        return ResponseEntity.ok(adoptionService.fetchAdoptionSurveysByUserEmail(principal.getName()));
    }

    @Operation(summary = "Make up decision whether accept or not")
    @PostMapping("/adoption-surveys/decision")
    public ResponseEntity<Void> acceptAdoptionSurvey(@RequestBody AdoptionSurveyDecisionDTO decision) {
        adoptionService.makeUpDecision(decision.id(), decision.decision(), decision.message());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete survey by id")
    @DeleteMapping("/adoption-surveys/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id, Principal principal) {
        adoptionService.deleteSurveyById(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
