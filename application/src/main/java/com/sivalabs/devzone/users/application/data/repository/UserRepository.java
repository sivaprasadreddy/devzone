package com.sivalabs.devzone.users.application.data.repository;

import com.sivalabs.devzone.users.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    User save(User user);

    void delete(User user);

    void updatePassword(String email, String password);
}
