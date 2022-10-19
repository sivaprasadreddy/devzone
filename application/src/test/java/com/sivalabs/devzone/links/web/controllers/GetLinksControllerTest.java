package com.sivalabs.devzone.links.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.common.PagedResult;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.services.LinkService;
import com.sivalabs.devzone.links.web.mappers.LinkDtoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;

@WebMvcTest(controllers = GetLinksController.class)
public class GetLinksControllerTest extends AbstractWebMvcTest {

    @MockBean protected LinkService linkService;
    @SpyBean protected LinkDtoMapper linkDTOMapper;

    @Test
    void shouldFetchLinksFirstPage() throws Exception {
        PagedResult<Link> linksDTO = new PagedResult<>(Page.empty());
        given(linkService.getLinks(any(Integer.class))).willReturn(linksDTO);

        this.mockMvc
                .perform(get("/links"))
                .andExpect(status().isOk())
                .andExpect(view().name("links"))
                .andExpect(model().attributeExists("linksData"));
    }

    @Test
    void shouldFetchLinksByCategory() throws Exception {
        PagedResult<Link> linksDTO = new PagedResult<>(Page.empty());
        given(linkService.getLinksByCategory(anyString(), any(Integer.class))).willReturn(linksDTO);

        this.mockMvc
                .perform(get("/links?category=java"))
                .andExpect(status().isOk())
                .andExpect(view().name("links"))
                .andExpect(model().attributeExists("linksData", "header"));
    }

    @Test
    void shouldFetchLinksBySearchKey() throws Exception {
        PagedResult<Link> linksDTO = new PagedResult<>(Page.empty());
        given(linkService.searchLinks(anyString(), any(Integer.class))).willReturn(linksDTO);

        this.mockMvc
                .perform(get("/links?query=java"))
                .andExpect(status().isOk())
                .andExpect(view().name("links"))
                .andExpect(model().attributeExists("linksData", "header"));
    }
}
