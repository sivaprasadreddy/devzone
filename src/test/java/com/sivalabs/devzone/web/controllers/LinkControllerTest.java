package com.sivalabs.devzone.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.domain.models.LinksDTO;
import com.sivalabs.devzone.domain.services.LinkService;
import com.sivalabs.devzone.domain.services.SecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

@WebMvcTest(controllers = LinkController.class)
public class LinkControllerTest extends AbstractWebMvcTest {

    @MockBean protected LinkService linkService;

    @MockBean protected SecurityService securityService;

    @Test
    void shouldFetchLinksFirstPage() throws Exception {
        LinksDTO linksDTO = new LinksDTO();
        given(linkService.getAllLinks(any(Pageable.class))).willReturn(linksDTO);

        this.mockMvc
                .perform(get("/links"))
                .andExpect(status().isOk())
                .andExpect(view().name("links"))
                .andExpect(model().attributeExists("linksData"));
    }

    @Test
    void shouldFetchLinksByTag() throws Exception {
        LinksDTO linksDTO = new LinksDTO();
        given(linkService.getLinksByTag(anyString(), any(Pageable.class))).willReturn(linksDTO);

        this.mockMvc
                .perform(get("/links?tag=java"))
                .andExpect(status().isOk())
                .andExpect(view().name("links"))
                .andExpect(model().attributeExists("linksData", "header"));
    }

    @Test
    void shouldFetchLinksBySearchKey() throws Exception {
        LinksDTO linksDTO = new LinksDTO();
        given(linkService.searchLinks(anyString(), any(Pageable.class))).willReturn(linksDTO);

        this.mockMvc
                .perform(get("/links?query=java"))
                .andExpect(status().isOk())
                .andExpect(view().name("links"))
                .andExpect(model().attributeExists("linksData", "header"));
    }
}
