package com.sivalabs.devzone.web.api.resources;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.models.ChangePasswordRequest;
import com.sivalabs.devzone.domain.models.CreateUserRequest;
import com.sivalabs.devzone.domain.models.UserDTO;
import com.sivalabs.devzone.domain.services.UserService;
import com.sivalabs.devzone.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRestControllerIT extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void should_find_user_by_id() throws Exception {
        Long userId = 1L;
        this.mockMvc.perform(get("/api/users/{id}", userId)).andExpect(status().isOk());
    }

    @Test
    void should_create_new_user_with_valid_data() throws Exception {
        CreateUserRequest createUserRequestDTO = CreateUserRequest.builder().email("myemail@gmail.com")
            .password("secret").name("myname").build();

        this.mockMvc
            .perform(post("/api/users").contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequestDTO)))
            .andExpect(status().isCreated());

    }

    @Test
    void should_fail_to_create_new_user_with_existing_email() throws Exception {
        CreateUserRequest createUserRequestDTO = CreateUserRequest.builder().email("admin@gmail.com")
            .password("secret").name("myname").build();

        this.mockMvc
            .perform(post("/api/users").contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequestDTO)))
            .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser("siva1@gmail.com")
    void should_update_own_user_details_when_user_is_authorized() throws Exception {
        User savedUser = createUser("siva1@gmail.com");

        savedUser.setName("New name");

        this.mockMvc.perform(put("/api/users/" + savedUser.getId()).contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(savedUser))).andExpect(status().isOk());

    }

    @Test
    @WithMockUser("siva@gmail.com")
    void should_not_update_other_user_details_when_user_is_authorized() throws Exception {
        User savedUser = createUser("siva2@gmail.com");
        savedUser.setName("New name");

        this.mockMvc.perform(put("/api/users/" + savedUser.getId()).contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(savedUser))).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser("admin@gmail.com")
    void should_fail_to_delete_non_existing_user() throws Exception {
        this.mockMvc.perform(delete("/api/users/{id}", 9999)).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser("admin@gmail.com")
    void should_be_able_to_delete_other_user_if_user_is_admin() throws Exception {
        User savedUser = createUser("someuser@gmail.com");

        this.mockMvc.perform(delete("/api/users/{id}", savedUser.getId())).andExpect(status().isOk());
    }

    @Test
    @WithMockUser("siva@gmail.com")
    void should_not_be_able_to_delete_other_user_if_not_admin() throws Exception {
        User savedUser = createUser("user123@gmail.com");
        this.mockMvc.perform(delete("/api/users/{id}", savedUser.getId())).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser("siva@gmail.com")
    void should_update_password_when_user_is_authorized() throws Exception {
        ChangePasswordRequest changePasswordDTO = ChangePasswordRequest.builder().oldPassword("siva").newPassword("newpwd")
            .build();

        this.mockMvc.perform(post("/api/user/change-password").contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(changePasswordDTO))).andExpect(status().isOk());

    }

    @Test
    void should_fail_to_update_password_when_user_is_not_authorized() throws Exception {
        ChangePasswordRequest changePasswordDTO = ChangePasswordRequest.builder().oldPassword("admin").newPassword("newpwd")
            .build();

        this.mockMvc.perform(post("/api/user/change-password").contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(changePasswordDTO))).andExpect(status().isForbidden());

    }

    private User createUser(String email) {
        User user = TestDataFactory.createUser(email);
        String plainPwd = user.getPassword();
        UserDTO userDTO = userService.createUser(UserDTO.fromEntity(user));
        user.setId(userDTO.getId());
        user.setPassword(plainPwd);
        return user;
    }

}
