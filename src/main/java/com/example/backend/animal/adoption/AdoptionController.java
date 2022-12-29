package com.example.backend.animal.adoption;

import com.example.backend.animal.adoption.dto.WalkDTO;
import com.example.backend.animal.adoption.dto.WalkDetailsOutput;
import com.example.backend.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
