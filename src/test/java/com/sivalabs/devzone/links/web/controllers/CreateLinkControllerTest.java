package com.sivalabs.devzone.links.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.links.domain.models.CreateLinkRequest;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.services.LinkService;
import com.sivalabs.devzone.users.domain.models.User;
import com.sivalabs.devzone.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = CreateLinkController.class)
public class CreateLinkControllerTest extends AbstractWebMvcTest {

    @MockBean protected LinkService linkService;

    @Test
    void shouldShowCreateLinkFormPage() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);

        mockMvc.perform(get("/links/new").with(user(securityUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("add-link"))
                .andExpect(model().attributeExists("link"));
    }

    @Test
    void shouldCreateLinkSuccessfully() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Link link = new Link();
        given(linkService.createLink(any(CreateLinkRequest.class))).willReturn(link);
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
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Link link = new Link();
        given(linkService.createLink(any(CreateLinkRequest.class))).willReturn(link);

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
}
