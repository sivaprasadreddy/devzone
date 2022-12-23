package com.sivalabs.devzone.users.domain.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.sivalabs.devzone.common.exceptions.DevZoneException;
import com.sivalabs.devzone.users.domain.models.ChangePasswordRequest;
import com.sivalabs.devzone.users.domain.models.RoleEnum;
import com.sivalabs.devzone.users.domain.models.User;
import com.sivalabs.devzone.users.domain.repositories.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userRepository = mock(UserRepository.class);
        userService = new UserService(passwordEncoder, userRepository);
    }

    @Test
    void shouldUpdatePasswordIfValid() {
        String email = "siva@gmail.com";
        User user =
                new User(1L, "user", email, passwordEncoder.encode("siva123"), RoleEnum.ROLE_USER);
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        ChangePasswordRequest request = new ChangePasswordRequest("siva123", "siva456");
        userService.changePassword(email, request);
    }

    @Test
    void shouldThrowErrorIfCurrentPasswordIsInvalid() {
        String email = "siva@gmail.com";
        User user =
                new User(
                        1L,
                        "user",
                        email,
                        passwordEncoder.encode("incorrect-password"),
                        RoleEnum.ROLE_USER);
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        ChangePasswordRequest request = new ChangePasswordRequest("siva123", "siva456");
        assertThrows(DevZoneException.class, () -> userService.changePassword(email, request));
    }
}
