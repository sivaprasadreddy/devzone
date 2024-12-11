package com.sivalabs.devzone.users.application.usecases.registration;

import com.sivalabs.devzone.common.exceptions.ResourceAlreadyExistsException;
import com.sivalabs.devzone.users.application.data.repository.UserRepository;
import com.sivalabs.devzone.users.domain.model.RoleEnum;
import com.sivalabs.devzone.users.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateUserHandler {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public CreateUserHandler(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserRequest createUserRequest) {
        if (userRepository.existsByEmail(createUserRequest.email())) {
            throw new ResourceAlreadyExistsException(
                    "User with email " + createUserRequest.email() + " already exists");
        }
        String encPwd = passwordEncoder.encode(createUserRequest.password());
        User user = new User(null, createUserRequest.name(), createUserRequest.email(), encPwd, RoleEnum.ROLE_USER);
        return userRepository.save(user);
    }
}
