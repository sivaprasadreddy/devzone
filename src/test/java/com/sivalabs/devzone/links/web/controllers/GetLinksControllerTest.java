package com.sivalabs.devzone.links.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.links.domain.models.LinksDTO;
import com.sivalabs.devzone.links.domain.services.LinkService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = GetLinksController.class)
public class GetLinksControllerTest extends AbstractWebMvcTest {

    @MockBean protected LinkService linkService;

    @Test
    void shouldFetchLinksFirstPage() throws Exception {
        LinksDTO linksDTO = new LinksDTO();
        given(linkService.getAllLinks(any(Integer.class))).willReturn(linksDTO);

        this.mockMvc
                .perform(get("/links"))
                .andExpect(status().isOk())
                .andExpect(view().name("links"))
                .andExpect(model().attributeExists("linksData"));
    }

    @Test
    void shouldFetchLinksByCategory() throws Exception {
        LinksDTO linksDTO = new LinksDTO();
        given(linkService.getLinksByCategory(anyString(), any(Integer.class))).willReturn(linksDTO);

        this.mockMvc
                .perform(get("/links?category=java"))
                .andExpect(status().isOk())
                .andExpect(view().name("links"))
                .andExpect(model().attributeExists("linksData", "header"));
    }

    @Test
    void shouldFetchLinksBySearchKey() throws Exception {
        LinksDTO linksDTO = new LinksDTO();
        given(linkService.searchLinks(anyString(), any(Integer.class))).willReturn(linksDTO);

        this.mockMvc
                .perform(get("/links?query=java"))
                .andExpect(status().isOk())
                .andExpect(view().name("links"))
                .andExpect(model().attributeExists("linksData", "header"));
    }
}
