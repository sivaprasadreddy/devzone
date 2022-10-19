package com.sivalabs.devzone.users.domain.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.sivalabs.devzone.common.exceptions.DevZoneException;
import com.sivalabs.devzone.users.domain.models.ChangePasswordRequest;
import com.sivalabs.devzone.users.domain.models.User;
import com.sivalabs.devzone.users.domain.repositories.UserRepository;
import com.sivalabs.devzone.utils.TestDataFactory;
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
        User user = TestDataFactory.getMockUser();
        user.setPassword(passwordEncoder.encode("siva123"));
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        ChangePasswordRequest request =
                ChangePasswordRequest.builder()
                        .oldPassword("siva123")
                        .newPassword("siva456")
                        .build();
        userService.changePassword(email, request);
    }

    @Test
    void shouldThrowErrorIfCurrentPasswordIsInvalid() {
        String email = "siva@gmail.com";
        User user = TestDataFactory.getMockUser();
        user.setPassword(passwordEncoder.encode("incorrect-password"));
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        ChangePasswordRequest request =
                ChangePasswordRequest.builder()
                        .oldPassword("siva123")
                        .newPassword("siva456")
                        .build();
        assertThrows(DevZoneException.class, () -> userService.changePassword(email, request));
    }
}
