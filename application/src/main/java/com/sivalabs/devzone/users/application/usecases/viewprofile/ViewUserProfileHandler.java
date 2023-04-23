package com.sivalabs.devzone.users.application.usecases.viewprofile;

import com.sivalabs.devzone.users.application.data.repository.UserRepository;
import com.sivalabs.devzone.users.domain.model.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ViewUserProfileHandler {
    private final UserRepository userRepository;

    public ViewUserProfileHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
