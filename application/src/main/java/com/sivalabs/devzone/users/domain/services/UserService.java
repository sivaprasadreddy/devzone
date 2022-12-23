package com.sivalabs.devzone.users.domain.services;

import com.sivalabs.devzone.common.exceptions.DevZoneException;
import com.sivalabs.devzone.common.exceptions.ResourceAlreadyExistsException;
import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.users.domain.models.ChangePasswordRequest;
import com.sivalabs.devzone.users.domain.models.CreateUserRequest;
import com.sivalabs.devzone.users.domain.models.RoleEnum;
import com.sivalabs.devzone.users.domain.models.User;
import com.sivalabs.devzone.users.domain.repositories.UserRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(CreateUserRequest createUserRequest) {
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "User with email " + createUserRequest.getEmail() + " already exists");
        }
        User user = new User();
        user.setName(createUserRequest.getName());
        user.setEmail(createUserRequest.getEmail());
        String encPwd = passwordEncoder.encode(createUserRequest.getPassword());
        user.setPassword(encPwd);
        user.setRole(RoleEnum.ROLE_USER);
        return userRepository.save(user);
    }

    public void changePassword(String email, ChangePasswordRequest changePasswordRequest) {
        User user = this.getUserByEmail(email).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException("User with email " + email + " not found");
        }
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new DevZoneException("Current password doesn't match");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }
}
