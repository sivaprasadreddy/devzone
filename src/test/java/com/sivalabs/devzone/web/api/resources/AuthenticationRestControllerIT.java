package com.sivalabs.devzone.web.api.resources;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import com.sivalabs.devzone.config.ApplicationProperties;
import com.sivalabs.devzone.config.security.TokenHelper;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.models.AuthenticationRequest;
import com.sivalabs.devzone.domain.models.UserDTO;
import com.sivalabs.devzone.domain.services.UserService;
import com.sivalabs.devzone.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationRestControllerIT extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private ApplicationProperties properties;

    @Test
    void should_login_successfully_with_valid_credentials() throws Exception {
        User user = createUser();
        AuthenticationRequest authenticationRequestDTO = AuthenticationRequest.builder().username(user.getEmail())
            .password(user.getPassword()).build();

        this.mockMvc.perform(post("/api/auth/login").contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authenticationRequestDTO))).andExpect(status().isOk());
    }

    @Test
    void should_not_login_with_invalid_credentials() throws Exception {
        AuthenticationRequest authenticationRequestDTO = AuthenticationRequest.builder()
            .username("nonexisting@gmail.com").password("secret").build();

        this.mockMvc
            .perform(post("/api/auth/login").contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequestDTO)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser("siva@gmail.com")
    void should_get_refreshed_authToken_if_authorized() throws Exception {
        String token = tokenHelper.generateToken("siva@gmail.com");
        this.mockMvc.perform(
            post("/api/auth/refresh").header(properties.getJwt().getHeader(), "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    void should_fail_to_get_refreshed_authToken_if_unauthorized() throws Exception {
        this.mockMvc.perform(post("/api/auth/refresh")).andExpect(status().isForbidden());
    }

    @Test
    void should_fail_to_get_refreshed_authToken_if_token_is_invalid() throws Exception {
        this.mockMvc.perform(
            post("/api/auth/refresh").header(properties.getJwt().getHeader(), "Bearer invalid-token"))
            .andExpect(status().isForbidden());
    }

    private User createUser() {
        User user = TestDataFactory.createUser();
        String plainPwd = user.getPassword();
        UserDTO userDTO = userService.createUser(UserDTO.fromEntity(user));
        user.setId(userDTO.getId());
        user.setPassword(plainPwd);
        return user;
    }

}
