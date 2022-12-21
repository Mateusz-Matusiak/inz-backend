package com.example.backend.user;

import com.example.backend.exception.IncorrectProviderException;
import com.example.backend.security.TokenService;
import com.example.backend.user.dto.GoogleCredentialsDTO;
import com.example.backend.user.dto.RegisterUserDTO;
import com.example.backend.user.dto.UpdateUserDTO;
import com.example.backend.user.dto.UserOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

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

    @PatchMapping("/{id}")
    public ResponseEntity<UserOutput> updateUser(@RequestBody UpdateUserDTO userDetails,
                                                 @PathVariable Long id) {
        return userService.partialUpdate(id, userDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> fetchUserById(@PathVariable Long id) {
        return userService.getUserById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody @Valid RegisterUserDTO loginRequestDTO) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password()));
        return ResponseEntity.ok()
                .header(AUTHORIZATION, "Bearer " + tokenService.generateToken(authentication))
                .build();
    }

    @PostMapping("/login/google")
    public ResponseEntity<UserOutput> loginUserWithGoogle(@RequestBody @Valid GoogleCredentialsDTO googleCredentialsDTO) {
        UserEntity user = (UserEntity) userService.loadUserByUsername(googleCredentialsDTO.email());
        if (user != null) {
            if (user.getProvider() != CredentialsProvider.GOOGLE) {
                throw new IncorrectProviderException("This user is signed up by internal provider not google!");
            }
            return ResponseEntity.ok()
                    .header(AUTHORIZATION,
                            "Bearer " + tokenService.generateToken(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(googleCredentialsDTO.email(), ""))))
                    .build();
        }
        return userService.addGoogleUser(googleCredentialsDTO).map(userOutput ->
                        ResponseEntity
                                .created(URI.create(String.format("/users/%d", userOutput.id())))
                                .header(AUTHORIZATION, "Bearer " + tokenService.generateToken(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(googleCredentialsDTO.email(), ""))))
                                .body(userOutput)
                )
                .orElse(ResponseEntity.badRequest().build());
    }
}
