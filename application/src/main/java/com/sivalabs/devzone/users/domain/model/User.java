package com.sivalabs.devzone.users.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Arrays;

public record User(
        Long id,
        @NotBlank(message = "Name cannot be blank") String name,
        @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email address") String email,
        @NotBlank(message = "Password cannot be blank") @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
                String password,
        RoleEnum role) {

    public User(Long userId) {
        this(userId, null, null, null, null);
    }

    public boolean isAdminOrModerator() {
        return hasAnyRole(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_MODERATOR);
    }

    public boolean hasAnyRole(RoleEnum... roles) {
        return Arrays.asList(roles).contains(this.role());
    }
}
