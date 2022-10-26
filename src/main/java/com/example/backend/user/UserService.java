package com.example.backend.user;

import com.example.backend.exception.ResourceAlreadyExistsException;
import com.example.backend.exception.ResourceNotExistsException;
import com.example.backend.user.dto.RegisterUserDTO;
import com.example.backend.user.dto.UserMapper;
import com.example.backend.user.dto.UserOutput;
import com.example.backend.user.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public Optional<UserOutput> addUser(RegisterUserDTO newUser) {
        return Optional.ofNullable(roleRepository.findByName("USER")
                .map(
                        role -> {
                            if (userRepository.findByEmail(newUser.email()).isEmpty()) {
                                return userRepository.save(
                                        userMapper.map(newUser));
                            } else {
                                throw new ResourceAlreadyExistsException(String.format("Email %s already exists", newUser.email()));
                            }
                        })
                .map(userMapper::map)
                .orElseThrow(() -> {
                    final String message = "Role USER does not exist!!";
                    log.error(message);
                    throw new ResourceNotExistsException(message);
                }));
    }

    public List<UserOutput> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::map)
                .toList();
    }

}
