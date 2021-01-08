package com.sivalabs.devzone.web.api.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

class LinkRestControllerIT extends AbstractIntegrationTest {

    @Test
    void shouldFetchLinksFirstPage() throws Exception {
        this.mockMvc.perform(get("/api/links")).andExpect(status().isOk());
    }

    @Test
    void shouldFetchLinksSecondPage() throws Exception {
        this.mockMvc.perform(get("/api/links?page=2")).andExpect(status().isOk());
    }

    @Test
    void shouldFetchLinksByTag() throws Exception {
        this.mockMvc.perform(get("/api/links?tag=spring-boot")).andExpect(status().isOk());
    }

    @Test
    void shouldSearchLinks() throws Exception {
        this.mockMvc.perform(get("/api/links?query=spring")).andExpect(status().isOk());
    }
}
