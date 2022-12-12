package com.example.backend.user.dto;

import com.example.backend.user.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserOutput map(UserEntity user);

    UserEntity map(RegisterUserDTO user);

    List<UserOutput> map(List<UserEntity> users);

    UserEntity map(GoogleCredentialsDTO user);
}
