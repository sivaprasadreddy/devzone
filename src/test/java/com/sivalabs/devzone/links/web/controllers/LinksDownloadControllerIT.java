package com.sivalabs.devzone.links.web.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

class LinksDownloadControllerIT extends AbstractIntegrationTest {

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    void shouldDownloadBookmarks() throws Exception {
        mockMvc.perform(get("/links/download")).andExpect(status().isOk());
    }
}
