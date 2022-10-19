package com.sivalabs.devzone.users.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private RoleEnum role;

    public boolean isAdminOrModerator() {
        return hasAnyRole(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_MODERATOR);
    }

    public boolean hasAnyRole(RoleEnum... roles) {
        return Arrays.asList(roles).contains(this.getRole());
    }
}
