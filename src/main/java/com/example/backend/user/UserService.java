package com.example.backend.user;

import com.example.backend.user.dto.RegisterUserDTO;
import com.example.backend.user.dto.UserMapper;
import com.example.backend.user.dto.UserOutput;
import com.example.backend.user.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public Optional<UserOutput> addUser(RegisterUserDTO newUser) {
        return roleRepository.findByName("USER")
                .map(
                        role -> userRepository.save(
                                userMapper.map(newUser)))
                .map(userMapper::map);
    }

    public List<UserOutput> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::map)
                .toList();
    }

}
