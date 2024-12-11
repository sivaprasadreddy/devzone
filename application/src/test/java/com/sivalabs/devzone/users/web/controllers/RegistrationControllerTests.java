package com.sivalabs.devzone.users.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

class RegistrationControllerTests extends AbstractIntegrationTest {

    @Test
    void shouldRegisterSuccessfully() throws Exception {
        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .param("name", "dummy")
                        .param("email", "dummy@mail.com")
                        .param("password", "admin1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("msg", "Registration is successful"))
                .andExpect(header().string("Location", "/login"));
    }

    @Test
    void shouldRedisplayRegistrationFormPageWhenEmailAlreadyExists() throws Exception {
        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .param("name", "Siva")
                        .param("email", "siva@gmail.com")
                        .param("password", "siva"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "email"))
                .andExpect(model().attributeHasFieldErrorCode("user", "email", "email.exists"))
                .andExpect(view().name("registration"));
    }
}
