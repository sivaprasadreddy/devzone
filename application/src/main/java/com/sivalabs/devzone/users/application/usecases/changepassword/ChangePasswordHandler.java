package com.sivalabs.devzone.users.application.usecases.changepassword;

import com.sivalabs.devzone.common.exceptions.DevZoneException;
import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.users.application.data.repository.UserRepository;
import com.sivalabs.devzone.users.domain.model.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChangePasswordHandler {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public ChangePasswordHandler(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void changePassword(String email, ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException("User with email " + email + " not found");
        }
        if (!passwordEncoder.matches(changePasswordRequest.oldPassword(), user.password())) {
            throw new DevZoneException("Current password doesn't match");
        }
        var pwd = passwordEncoder.encode(changePasswordRequest.newPassword());
        userRepository.updatePassword(email, pwd);
    }
}
