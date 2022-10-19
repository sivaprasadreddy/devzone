package com.sivalabs.devzone.links.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.services.LinkService;
import com.sivalabs.devzone.users.domain.models.RoleEnum;
import com.sivalabs.devzone.users.domain.models.User;
import com.sivalabs.devzone.utils.TestDataFactory;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = DeleteLinkController.class)
public class DeleteLinkControllerTest extends AbstractWebMvcTest {
    @MockBean protected LinkService linkService;

    @Test
    void shouldBeAbleToDeleteOwnLinkSuccessfully() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Link link = TestDataFactory.getMockLink(1L, 1L);
        given(linkService.getLinkById(any(Long.class))).willReturn(Optional.of(link));

        mockMvc.perform(delete("/links/{id}", 1).with(csrf()).with(user(securityUser)))
                .andExpect(status().isOk());
    }

    @Test
    void adminShouldBeAbleToDeleteOthersLinkSuccessfully() throws Exception {
        User user = TestDataFactory.getMockUser(1L, RoleEnum.ROLE_ADMIN);
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Link link = TestDataFactory.getMockLink(1L, 1L);
        given(linkService.getLinkById(any(Long.class))).willReturn(Optional.of(link));

        mockMvc.perform(delete("/links/{id}", 1).with(csrf()).with(user(securityUser)))
                .andExpect(status().isOk());
    }

    @Test
    void normalUserShouldNotBeAbleToDeleteOthersLink() throws Exception {
        User user = TestDataFactory.getMockUser(1L, RoleEnum.ROLE_USER);
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Link link = TestDataFactory.getMockLink(1L, 2L);
        given(linkService.getLinkById(any(Long.class))).willReturn(Optional.of(link));

        mockMvc.perform(delete("/links/{id}", 1).with(csrf()).with(user(securityUser)))
                .andExpect(status().isForbidden());
    }

    @Test
    void return404IfLinkNotExists() throws Exception {
        User user = TestDataFactory.getMockUser(1L, RoleEnum.ROLE_USER);
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        given(linkService.getLinkById(any(Long.class))).willReturn(Optional.empty());

        mockMvc.perform(delete("/links/{id}", 1).with(csrf()).with(user(securityUser)))
                .andExpect(status().isNotFound());
    }
}
