package com.sivalabs.devzone.links.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.security.test.context.support.WithUserDetails;

class LinkControllerIT extends AbstractIntegrationTest {

    @ParameterizedTest
    @CsvSource({"1", "2"})
    void shouldShowLinksByPage(int pageNo) throws Exception {
        mockMvc.perform(get("/links?page={page}", pageNo))
                .andExpect(status().isOk())
                .andExpect(view().name("links"))
                .andExpect(model().attributeExists("paginationPrefix"))
                .andExpect(model().attributeExists("linksData"))
                .andExpect(model().attributeExists("categories"));
    }

    @ParameterizedTest
    @CsvSource({"spring", "gradle"})
    void shouldSearchLinksByQuery(String query) throws Exception {
        mockMvc.perform(get("/links?query={query}", query))
                .andExpect(status().isOk())
                .andExpect(view().name("links"))
                .andExpect(model().attributeExists("paginationPrefix"))
                .andExpect(model().attributeExists("header"))
                .andExpect(model().attributeExists("linksData"))
                .andExpect(model().attributeExists("categories"));
    }

    @ParameterizedTest
    @CsvSource({"spring-boot", "gradle"})
    void shouldFetchLinksByCategory(String category) throws Exception {
        mockMvc.perform(get("/links?category={category}", category))
                .andExpect(status().isOk())
                .andExpect(view().name("links"))
                .andExpect(model().attributeExists("paginationPrefix"))
                .andExpect(model().attributeExists("header"))
                .andExpect(model().attributeExists("linksData"))
                .andExpect(model().attributeExists("categories"));
    }

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
