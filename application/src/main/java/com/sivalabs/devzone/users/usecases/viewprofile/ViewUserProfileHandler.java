package com.sivalabs.devzone.users.usecases.viewprofile;

import com.sivalabs.devzone.users.domain.User;
import com.sivalabs.devzone.users.gateways.data.repository.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
