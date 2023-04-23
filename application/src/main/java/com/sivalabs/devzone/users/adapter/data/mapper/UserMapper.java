package com.sivalabs.devzone.users.adapter.data.mapper;

import com.sivalabs.devzone.users.adapter.data.entity.UserEntity;
import com.sivalabs.devzone.users.domain.model.User;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.id());
        userEntity.setName(user.name());
        userEntity.setEmail(user.email());
        userEntity.setPassword(user.password());
        userEntity.setRole(user.role());
        return userEntity;
    }

    public User toModel(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRole());
    }
}
