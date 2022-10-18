package com.example.backend.user;

import com.example.backend.user.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Optional<UserOutput> addUser(RegisterUserDTO newUser) {
        return roleRepository.findByName("USER").map(
                role -> userRepository.save(new UserEntity(null, null, newUser.email(), newUser.password(), null, role))
        ).map(
                user -> new UserOutput(user.getId(), user.getFirstName(),
                        user.getLastName(), user.getEmail(),
                        user.getPassword(), user.getPhoneNumber(),
                        user.getRole().getName())
        );

    }

}
