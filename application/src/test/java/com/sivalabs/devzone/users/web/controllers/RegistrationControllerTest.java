package com.sivalabs.devzone.users.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.common.exceptions.ResourceAlreadyExistsException;
import com.sivalabs.devzone.users.application.usecases.registration.CreateUserHandler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = RegistrationController.class)
class RegistrationControllerTest extends AbstractWebMvcTest {

    @MockBean private CreateUserHandler createUserHandler;

    @Test
    void shouldShowRegistrationFormPage() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void shouldRedisplayRegistrationFormPageWhenSubmittedInvalidData() throws Exception {
        given(createUserHandler.createUser(any())).willAnswer(ans -> ans.getArgument(0));

        mockMvc.perform(
                        post("/registration")
                                .with(csrf())
                                .param("name", "")
                                .param("email", "")
                                .param("password", ""))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "name", "email", "password"))
                .andExpect(model().attributeHasFieldErrorCode("user", "name", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("user", "email", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("user", "password", "NotBlank"))
                .andExpect(view().name("registration"));
    }

    @Test
    void shouldRedisplayRegistrationFormPageWhenEmailAlreadyExists() throws Exception {
        given(createUserHandler.createUser(any()))
                .willThrow(new ResourceAlreadyExistsException("user already exists"));
        mockMvc.perform(
                        post("/registration")
                                .with(csrf())
                                .param("name", "dummy")
                                .param("email", "dummy@mail.com")
                                .param("password", "admin1234"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "email"))
                .andExpect(model().attributeHasFieldErrorCode("user", "email", "email.exists"))
                .andExpect(view().name("registration"));
    }
}
