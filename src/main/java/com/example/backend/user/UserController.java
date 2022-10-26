package com.example.backend.user;

import com.example.backend.user.dto.RegisterUserDTO;
import com.example.backend.user.dto.UserOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserOutput> registerUser(@RequestBody @Valid RegisterUserDTO user) {
        return userService.addUser(user)
                .map(userOutput ->
                        ResponseEntity.created(URI.create(String.format("/users/%d", userOutput.id())))
                                .body(userOutput))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping
    public ResponseEntity<List<UserOutput>> fetchAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
