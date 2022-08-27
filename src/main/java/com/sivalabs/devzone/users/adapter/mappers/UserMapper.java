package com.sivalabs.devzone.users.adapter.mappers;

import com.sivalabs.devzone.users.adapter.entities.UserEntity;
import com.sivalabs.devzone.users.domain.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setImageUrl(user.getImageUrl());
        userEntity.setRole(user.getRole());
        return userEntity;
    }

    public User toDTO(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setName(userEntity.getName());
        user.setEmail(userEntity.getEmail());
        user.setPassword(userEntity.getPassword());
        user.setImageUrl(userEntity.getImageUrl());
        user.setRole(userEntity.getRole());
        return user;
    }
}
