package com.sivalabs.devzone.links.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.links.domain.models.LinkDTO;
import com.sivalabs.devzone.links.domain.models.LinksDTO;
import com.sivalabs.devzone.links.domain.services.CategoryService;
import com.sivalabs.devzone.links.domain.services.LinkService;
import com.sivalabs.devzone.users.domain.models.RoleEnum;
import com.sivalabs.devzone.users.domain.models.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = LinkController.class)
public class LinkControllerTest extends AbstractWebMvcTest {

    @MockBean protected LinkService linkService;

    @MockBean protected CategoryService categoryService;

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

    @Test
    void shouldShowCreateLinkFormPage() throws Exception {
        User user = getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);

        mockMvc.perform(get("/links/new").with(user(securityUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("add-link"))
                .andExpect(model().attributeExists("link"));
    }

    @Test
    void shouldCreateLinkSuccessfully() throws Exception {
        User user = getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        LinkDTO linkDTO = new LinkDTO();
        given(linkService.createLink(any(LinkDTO.class))).willReturn(linkDTO);
        mockMvc.perform(
                        post("/links")
                                .with(csrf())
                                .with(user(securityUser))
                                .param("url", "https://sivalabs.in")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/links"));
    }

    @Test
    void shouldFailToCreateLinkIfUrlIsEmpty() throws Exception {
        User user = getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);

        LinkDTO linkDTO = new LinkDTO();
        given(linkService.createLink(any(LinkDTO.class))).willReturn(linkDTO);
        mockMvc.perform(
                        post("/links")
                                .with(csrf())
                                .with(user(securityUser))
                                .param("url", "")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("link", "url"))
                .andExpect(view().name("add-link"));
    }

    @Test
    void shouldShowEditLinkFormPage() throws Exception {
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setCreatedUserId(1L);
        User user = getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        given(linkService.getLinkById(any(Long.class))).willReturn(Optional.of(linkDTO));

        mockMvc.perform(get("/links/edit/{id}", 1).with(user(securityUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-link"))
                .andExpect(model().attributeExists("link"));
    }

    @Test
    void shouldUpdateLinkSuccessfully() throws Exception {
        User user = getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        LinkDTO linkDTO = new LinkDTO();
        given(linkService.updateLink(any(LinkDTO.class))).willReturn(linkDTO);
        mockMvc.perform(
                        put("/links/{id}", 1)
                                .with(csrf())
                                .with(user(securityUser))
                                .param("url", "https://sivalabs.in")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/links"));
    }

    @Test
    void shouldBeAbleToDeleteOwnLinkSuccessfully() throws Exception {
        User user = getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setId(1L);
        linkDTO.setCreatedUserId(1L);
        given(linkService.getLinkById(any(Long.class))).willReturn(Optional.of(linkDTO));
        mockMvc.perform(delete("/links/{id}", 1).with(csrf()).with(user(securityUser)))
                .andExpect(status().isOk());
    }

    @Test
    void adminShouldBeAbleToDeleteOthersLinkSuccessfully() throws Exception {
        User user = getMockUser(1L, RoleEnum.ROLE_ADMIN);
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        given(securityService.isUserAdminOrModerator(user)).willReturn(true);
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setId(1L);
        linkDTO.setCreatedUserId(2L);
        given(linkService.getLinkById(any(Long.class))).willReturn(Optional.of(linkDTO));
        mockMvc.perform(delete("/links/{id}", 1).with(csrf()).with(user(securityUser)))
                .andExpect(status().isOk());
    }

    private User getMockUser() {
        return getMockUser(1L, RoleEnum.ROLE_USER);
    }

    private User getMockUser(Long id, RoleEnum role) {
        User user = new User();
        user.setId(id);
        user.setEmail("user@gmail.com");
        user.setPassword("user");
        user.setRole(role);
        return user;
    }
}
