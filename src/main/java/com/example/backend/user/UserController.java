package com.example.backend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    public ResponseEntity<UserOutput> registerUser(RegisterUserDTO user) throws Exception {
        return userService.addUser(user)
                .map(userOutput ->
                        ResponseEntity.created(URI.create(String.format("/users/%d", userOutput.id())))
                                .body(userOutput))
                .orElseThrow(Exception::new);
    }
}
