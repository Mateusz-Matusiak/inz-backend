package com.example.backend.user;

import com.example.backend.exception.IncorrectProviderException;
import com.example.backend.mail.RegistrationEvent;
import com.example.backend.mail.UserToken;
import com.example.backend.mail.activation.token.ActivationTokenService;
import com.example.backend.security.TokenService;
import com.example.backend.user.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final ActivationTokenService activationTokenService;
    private final String callbackUrl = "http://localhost:3000/sign-in";

    @PostMapping
    public ResponseEntity<UserOutput> registerUser(@RequestBody @Valid RegisterUserDTO user) {
        return userService.addUser(user)
                .map(userOutput -> {
                            final String token = activationTokenService.createActivationToken(userOutput.email());
                            eventPublisher.publishEvent(new RegistrationEvent(UserToken.builder().email(userOutput.email()).token(token).callbackUrl(callbackUrl).build()));
                            return ResponseEntity.created(URI.create(String.format("/users/%d", userOutput.id())))
                                    .body(userOutput);
                        }
                )
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/confirm-registration")
    public ResponseEntity<UserOutput> confirmEmail(@RequestParam("token") String token) {
        return userService.verifyUser(token).map(ResponseEntity::ok).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity<List<UserWithAddressOutput>> fetchAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserOutput> updateUser(@RequestBody UpdateUserDTO userDetails,
                                                 @PathVariable Long id) {
        return userService.partialUpdate(id, userDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }

    @PatchMapping("/details")
    public ResponseEntity<UserOutput> updateUser(@RequestBody UpdateUserDTO userDetails, Principal principal) {
        return userService.partialUpdateByEmail(principal.getName(), userDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsOutput> fetchUserById(@PathVariable Long id) {
        return userService.getUserById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/details")
    public ResponseEntity<UserDetailsOutput> fetchUserById(Principal principal) {
        return userService.getUserDetailsByEmail(principal.getName()).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody @Valid RegisterUserDTO loginRequestDTO) {
        final Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().header(AUTHORIZATION, "Bearer " + tokenService.generateToken(authentication))
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
