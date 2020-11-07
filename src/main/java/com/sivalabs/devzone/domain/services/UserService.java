package com.sivalabs.devzone.domain.services;

import com.sivalabs.devzone.domain.entities.Role;
import com.sivalabs.devzone.domain.entities.RoleEnum;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.exceptions.DevZoneException;
import com.sivalabs.devzone.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.domain.models.ChangePasswordRequest;
import com.sivalabs.devzone.domain.models.UserDTO;
import com.sivalabs.devzone.domain.repositories.RoleRepository;
import com.sivalabs.devzone.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(UserDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDTO createUser(UserDTO user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DevZoneException("Email " + user.getEmail() + " is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userEntity = user.toEntity();
        Optional<Role> roleUser = roleRepository.findByName(RoleEnum.ROLE_USER.name());
        userEntity.setRoles(Collections.singleton(roleUser.orElse(null)));
        return UserDTO.fromEntity(userRepository.save(userEntity));
    }

    public UserDTO updateUser(UserDTO user) {
        User userById = userRepository.findById(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + user.getId() + " not found"));
        User userEntity = user.toEntity();
        userEntity.setPassword(userById.getPassword());
        userEntity.setRoles(userById.getRoles());
        return UserDTO.fromEntity(userRepository.save(userEntity));
    }

    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresent(userRepository::delete);
    }

    public void changePassword(String email, ChangePasswordRequest changePasswordRequest) {
        User user = this.getUserByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new DevZoneException("Current password doesn't match");
        }
    }
}
