package com.sivalabs.devzone.web.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

class LinkControllerIT extends AbstractIntegrationTest {

    @Test
    void shouldFetchLinksFirstPage() throws Exception {
        this.mockMvc.perform(get("/links")).andExpect(status().isOk());
    }

    @Test
    void shouldFetchLinksByCategory() throws Exception {
        this.mockMvc.perform(get("/links?category=spring-boot")).andExpect(status().isOk());
    }

    @Test
    void shouldSearchLinks() throws Exception {
        this.mockMvc.perform(get("/links?query=spring")).andExpect(status().isOk());
    }
}
