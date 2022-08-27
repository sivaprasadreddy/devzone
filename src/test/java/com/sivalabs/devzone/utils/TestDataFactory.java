package com.sivalabs.devzone.utils;

import com.sivalabs.devzone.users.adapter.entities.UserEntity;
import java.util.UUID;

public class TestDataFactory {

    public static UserEntity createUser() {
        String uuid = UUID.randomUUID().toString();
        return createUser(uuid + "@gmail.com", uuid);
    }

    public static UserEntity createUser(String email) {
        String uuid = UUID.randomUUID().toString();
        return createUser(email, uuid);
    }

    public static UserEntity createUser(String email, String password) {
        UserEntity user = new UserEntity();
        user.setName("someuser");
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
