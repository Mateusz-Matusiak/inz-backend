package com.example.backend.user;

import com.example.backend.exception.ResourceAlreadyExistsException;
import com.example.backend.exception.ResourceNotExistsException;
import com.example.backend.mail.activation.token.ActivationTokenRepository;
import com.example.backend.user.address.AddressEntity;
import com.example.backend.user.address.AddressRepository;
import com.example.backend.user.dto.*;
import com.example.backend.user.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final AddressRepository addressRepository;

    private final ActivationTokenRepository activationTokenRepository;

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

    public Optional<UserOutput> addGoogleUser(GoogleCredentialsDTO googleUser) {
        return Optional.ofNullable(roleRepository.findByName("USER")
                .map(
                        roleEntity -> {
                            if (userRepository.findByEmail(googleUser.email()).isEmpty()) {
                                return userRepository.save(userMapper.map(googleUser));
                            } else {
                                throw new ResourceAlreadyExistsException(String.format("Email %s already exists", googleUser.email()));
                            }
                        }
                )
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

    @Transactional
    public Optional<UserOutput> partialUpdate(Long id, UpdateUserDTO userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(checkAndUpdateField(user.getFirstName(), userDetails.firstName()));
                    user.setLastName(checkAndUpdateField(user.getLastName(), userDetails.lastName()));
                    user.setPhoneNumber(checkAndUpdateField(user.getPhoneNumber(), userDetails.phoneNumber()));
                    AddressEntity userAddress;
                    if (user.getAddress() != null) {
                        userAddress = user.getAddress();
                        addressRepository.deleteById(userAddress.getId());
                    } else {
                        userAddress = new AddressEntity();
                    }
                    userAddress.setCity(userDetails.city());
                    userAddress.setStreet(userDetails.streetName() + " " + userDetails.streetNumber());
                    userAddress.setPostalCode(userDetails.postalCode());
                    userAddress.setCountry(userDetails.country());
                    AddressEntity saved = addressRepository.save(userAddress);
                    user.setAddress(saved);
                    if (userDetails.role() != null) {
                        roleRepository.findByName(userDetails.role())
                                .ifPresentOrElse(
                                        user::setRole,
                                        () -> {
                                            log.warn("Role {} does not exist!", userDetails.role());
                                            throw new ResourceNotExistsException("Role " + userDetails.role() + " does not exist!");
                                        });
                    }
                    return userRepository.save(user);
                })
                .map(user -> Optional.of(userMapper.map(user)))
                .orElseThrow(() -> new ResourceNotExistsException(String.format("User with given id %d does not exist", id)));
    }

    private String checkAndUpdateField(String oldField, String newField) {
        if (newField != null && !newField.isBlank()) {
            return newField;
        }
        return oldField;
    }

    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }

    @Transactional
    public Optional<UserOutput> verifyUser(String token) {
        return activationTokenRepository.findById(token).flatMap(userToken -> {
            if (userToken.getExpireTime().isAfter(LocalDateTime.now())) {
                activationTokenRepository.delete(userToken);
                userToken.getUser().setActive(true);
                return Optional.of(userMapper.map(userRepository.save(userToken.getUser())));
            }
            log.warn("Token {} is already expired", userToken);
            return Optional.empty();
        }).or(() -> {
            log.warn("Token {} does not exist in database", token);
            return Optional.empty();
        });
    }
}
