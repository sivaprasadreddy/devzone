package com.sivalabs.devzone.posts.web.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

class PostsDownloadControllerIT extends AbstractIntegrationTest {

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    void shouldDownloadBookmarks() throws Exception {
        mockMvc.perform(get("/posts/download")).andExpect(status().isOk());
    }
}
