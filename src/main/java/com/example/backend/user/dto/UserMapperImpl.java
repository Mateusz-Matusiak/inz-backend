package com.example.backend.user.dto;

import com.example.backend.user.UserEntity;
import com.example.backend.user.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final RoleRepository roleRepository;

    @Override
    public UserOutput map(UserEntity user) {
        if (user == null)
            return null;
        return new UserOutput(user.getId(), user.getFirstName(),
                user.getLastName(), user.getEmail(),
                user.getPhoneNumber(), user.getRole().getName());
    }

    @Override
    public UserEntity map(RegisterUserDTO user) {
        if (user == null)
            return null;
        return roleRepository.findByName("USER")
                .map(
                        role -> new UserEntity("", "",
                                user.email(), user.password(),
                                "", role))
                .orElse(null);
    }

    @Override
    public List<UserOutput> map(List<UserEntity> users) {
        return users.stream()
                .map(
                        user -> new UserOutput(user.getId(), user.getFirstName(),
                                user.getLastName(), user.getEmail(),
                                user.getPhoneNumber(), user.getRole().getName()))
                .toList();
    }
}
