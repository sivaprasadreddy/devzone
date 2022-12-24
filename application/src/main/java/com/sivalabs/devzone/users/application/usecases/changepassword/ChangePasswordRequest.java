package com.sivalabs.devzone.users.application.usecases.changepassword;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank(message = "Old password cannot be blank") String oldPassword,
        @NotBlank(message = "New password cannot be blank") String newPassword) {}
