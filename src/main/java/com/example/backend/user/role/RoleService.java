package com.example.backend.user.role;

import com.example.backend.exception.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<RoleEntity> addRole(String name) {
        if (roleRepository.findByName(name).isPresent()) {
            String message = String.format("Role %s already exists in database", name);
            log.warn(message);
            throw new ResourceAlreadyExistsException(message);
        } else {
            return Optional.of(roleRepository.save(new RoleEntity(name)));
        }
    }

    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<RoleEntity> getRoleById(int id) {
        return roleRepository.findById(id);
    }
}
