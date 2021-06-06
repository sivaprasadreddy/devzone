package com.sivalabs.devzone.domain.services;

import com.sivalabs.devzone.domain.entities.RoleEnum;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.exceptions.ResourceAlreadyExistsException;
import com.sivalabs.devzone.domain.models.CreateUserRequest;
import com.sivalabs.devzone.domain.repositories.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createUser(CreateUserRequest createUserRequest) {
        if (getUserByEmail(createUserRequest.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException(
                    "User with email " + createUserRequest.getEmail() + " already exists");
        }
        User user = new User();
        user.setName(createUserRequest.getName());
        user.setEmail(createUserRequest.getEmail());
        String encPwd = passwordEncoder.encode(createUserRequest.getPassword());
        user.setPassword(encPwd);
        user.setRole(RoleEnum.ROLE_USER);
        userRepository.save(user);
    }
}
