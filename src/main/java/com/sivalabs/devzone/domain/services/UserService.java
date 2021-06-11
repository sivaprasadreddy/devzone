package com.sivalabs.devzone.domain.services;

import com.sivalabs.devzone.domain.entities.RoleEnum;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.exceptions.DevZoneException;
import com.sivalabs.devzone.domain.exceptions.ResourceAlreadyExistsException;
import com.sivalabs.devzone.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.domain.models.ChangePasswordRequest;
import com.sivalabs.devzone.domain.models.CreateUserRequest;
import com.sivalabs.devzone.domain.models.UserDTO;
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

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(UserDTO::fromEntity);
    }

    public UserDTO createUser(CreateUserRequest createUserRequest) {
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
        return UserDTO.fromEntity(userRepository.save(user));
    }

    public UserDTO updateUser(UserDTO user) {
        User userById =
                userRepository
                        .findById(user.getId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "User with id " + user.getId() + " not found"));
        User userEntity = user.toEntity();
        userEntity.setPassword(userById.getPassword());
        userEntity.setRole(userById.getRole());
        return UserDTO.fromEntity(userRepository.save(userEntity));
    }

    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresent(userRepository::delete);
    }

    public void changePassword(String email, ChangePasswordRequest changePasswordRequest) {
        User user =
                this.getUserByEmail(email)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "User with email " + email + " not found"));
        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new DevZoneException("Current password doesn't match");
        }
    }
}
