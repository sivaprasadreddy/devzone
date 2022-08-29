package com.sivalabs.devzone.links.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

public class CreateLinkControllerIT extends AbstractIntegrationTest {

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    void shouldCreateLinkSuccessfully() throws Exception {
        mockMvc.perform(
                        post("/links")
                                .with(csrf())
                                .param("url", "https://sivalabs.in")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/links"));
    }
}
