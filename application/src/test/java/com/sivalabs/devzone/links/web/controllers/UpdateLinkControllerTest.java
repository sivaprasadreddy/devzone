package com.sivalabs.devzone.links.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.models.UpdateLinkRequest;
import com.sivalabs.devzone.links.domain.services.LinkService;
import com.sivalabs.devzone.users.domain.models.RoleEnum;
import com.sivalabs.devzone.users.domain.models.User;
import com.sivalabs.devzone.utils.TestDataFactory;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = UpdateLinkController.class)
public class UpdateLinkControllerTest extends AbstractWebMvcTest {

    @MockBean protected LinkService linkService;

    @Test
    void shouldShowNotFoundWhenUpdatingLinkNotExists() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        given(linkService.getLinkById(any(Long.class))).willReturn(Optional.empty());

        mockMvc.perform(get("/links/{id}/edit", 1).with(user(securityUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldShowEditLinkFormPage() throws Exception {
        Link link = TestDataFactory.getMockLink(1L, 1L);
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        given(linkService.getLinkById(any(Long.class))).willReturn(Optional.of(link));

        mockMvc.perform(get("/links/{id}/edit", 1).with(user(securityUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-link"))
                .andExpect(model().attributeExists("link"));
    }

    @Test
    void shouldFailToUpdateLinkIfUrlIsEmpty() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Link link = TestDataFactory.getMockLink(1L, 1L);
        given(linkService.getLinkById(1L)).willReturn(Optional.of(link));
        given(linkService.updateLink(any(UpdateLinkRequest.class))).willReturn(link);

        mockMvc.perform(
                        put("/links/{id}", link.getId())
                                .with(csrf())
                                .with(user(securityUser))
                                .param("url", "")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("link", "url"))
                .andExpect(view().name("edit-link"));
    }

    @Test
    void shouldUpdateLinkSuccessfully() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Link link = TestDataFactory.getMockLink(1L, 1L);
        given(linkService.getLinkById(1L)).willReturn(Optional.of(link));
        given(linkService.updateLink(any(UpdateLinkRequest.class))).willReturn(link);

        mockMvc.perform(
                        put("/links/{id}", link.getId())
                                .with(csrf())
                                .with(user(securityUser))
                                .param("url", "https://sivalabs.in")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/links"));
    }

    @Test
    void shouldNotBeAbleToUpdateOthersLink() throws Exception {
        User user = TestDataFactory.getMockUser(1L, RoleEnum.ROLE_USER);
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Link link = TestDataFactory.getMockLink(1L, 2L);
        given(linkService.getLinkById(link.getId())).willReturn(Optional.of(link));
        given(linkService.updateLink(any(UpdateLinkRequest.class))).willReturn(link);

        mockMvc.perform(
                        put("/links/{id}", link.getId())
                                .with(csrf())
                                .with(user(securityUser))
                                .param("url", "https://sivalabs.in")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(status().is(403));
    }

    @Test
    void adminShouldBeAbleToUpdateOthersLink() throws Exception {
        User user = TestDataFactory.getMockUser(1L, RoleEnum.ROLE_ADMIN);
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Link link = TestDataFactory.getMockLink(1L, 2L);
        given(linkService.getLinkById(link.getId())).willReturn(Optional.of(link));
        given(linkService.updateLink(any(UpdateLinkRequest.class))).willReturn(link);

        mockMvc.perform(
                        put("/links/{id}", link.getId())
                                .with(csrf())
                                .with(user(securityUser))
                                .param("url", "https://sivalabs.in")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/links"));
    }
}
