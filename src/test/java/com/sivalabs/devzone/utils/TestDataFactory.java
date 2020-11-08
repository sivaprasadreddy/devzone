package com.sivalabs.devzone.utils;

import com.sivalabs.devzone.domain.entities.User;

import java.util.UUID;

public class TestDataFactory {

    public static User createUser() {
        String uuid = UUID.randomUUID().toString();
        return createUser(uuid + "@gmail.com", uuid);
    }

    public static User createUser(String email) {
        String uuid = UUID.randomUUID().toString();
        return createUser(email, uuid);
    }

    public static User createUser(String email, String password) {
        User user = new User();
        user.setName("someuser");
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
